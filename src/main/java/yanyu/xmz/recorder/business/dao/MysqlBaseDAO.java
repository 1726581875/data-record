package yanyu.xmz.recorder.business.dao;

import com.mysql.cj.jdbc.ConnectionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;
import yanyu.xmz.recorder.business.dao.annotation.TableField;
import yanyu.xmz.recorder.business.dao.obj.FieldDetail;
import yanyu.xmz.recorder.business.dao.util.ConnectionManagerUtil;
import yanyu.xmz.recorder.business.dao.util.NameConvertUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaomingzhang
 * @date 2021/12/29
 */
public class MysqlBaseDAO implements BaseDAO {

    private final static Set<Class<?>> supportTypeSet = new HashSet<>(Arrays.asList(String.class, Date.class));

    private static final String INSERT_TEMPLATE = "insert into `%s`(%s) values (%s)";

    private static final String UPDATE_TEMPLATE = "update %s set %s where %s = ?";

    private static final String CREATE_TABLE_SQL_TEMPLATE = "create table `%s` (\n%s\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

    /**
     * 存储列名对应的列信息
     */
    private static ThreadLocal<Map<String, FieldDetail>> fieldDetailMapThreadLocal = new ThreadLocal<>();


    private static final Logger log = LoggerFactory.getLogger(MysqlBaseDAO.class);

    public MysqlBaseDAO() {

    }

    @Override
    public <T> List<T> getList(String sql, Class<T> type) {
        List<T> resultList = new ArrayList<>();
        try (Connection connection = ConnectionManagerUtil.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql);
             ResultSet resultSet = prepareStatement.executeQuery()) {
            while (resultSet.next()) {
                resultList.add(analyzeResult(resultSet, type));
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private <T> T analyzeResult(ResultSet resultSet, Class<T> type) throws Exception {
        if (isMappingSupportType(type)) {
            return (T) resultSet.getObject(1);
        } else {
            T resultInstance = type.getConstructor().newInstance();
            Field[] declaredFields = type.getDeclaredFields();
            Set<String> tableColumnNameSet = getTableColumnNameSet(resultSet);
            for (Field field : declaredFields) {
                if (isMappingSupportType(field.getType())) {
                    // java列名转数据库命名规则，按驼峰对应“_”规则转换
                    String fieldName = NameConvertUtil.toDbRule(field.getName());
                    // 字段匹配，存在的列才获取结果并赋值,不存在的列则不做处理保持为null
                    if (tableColumnNameSet.contains(fieldName)) {
                        Object value = resultSet.getObject(fieldName);
                        if (Objects.nonNull(value)) {
                            // todo 通过setxxx方法设置值
                            field.setAccessible(true);
                            field.set(resultInstance, convertValue(field, value));
                        }
                    }
                }
            }
            return resultInstance;
        }
    }


    private Object convertValue(Field field, Object value) {
        // h2数据库tinyint查询结果对应java的byte类型，想使用Integer接收在此处做转换
        if (value instanceof Byte) {
            if (field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE)) {
                value = ((Byte) value).intValue();
            }
        }
        // fix: Can not set java.util.Date field EventRecord.createTime to java.time.LocalDateTime
        if(value instanceof LocalDateTime && field.getType() == Date.class){
            // 时区
            ZoneId zoneId = ZoneId.systemDefault();
            Instant instant = ((LocalDateTime)value).atZone(zoneId).toInstant();
            value = Date.from(instant);
        }

        return value;
    }


    @Override
    public <T> T getOne(String sql, Class<T> type) {
        List<T> resultList = getList(sql, type);
        if (resultList.size() == 0) {
            return null;
        }
        if (resultList.size() > 1) {
            throw new RuntimeException("返回结果行数大于1, 行数为" + resultList.size() + "");
        }
        return resultList.get(0);
    }

    @Override
    public <T> int insert(T obj) {
        if (obj == null) {
            throw new RuntimeException("插入元素为空, 请检查参数");
        }
        String insertSql = getInsertPrepareSQL(obj);
        log.debug(insertSql);
        try (Connection conn = ConnectionManagerUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertSql)) {
            return execUpdate(statement, obj);
        } catch (Exception e) {
            log.error("插入失败", e);
            throw new RuntimeException("数据库插入失败:" + e.getMessage());
        } finally {
            fieldDetailMapThreadLocal.remove();
        }
    }

    @Override
    public <T> Long insertReturnKey(T obj) {
        if (obj == null) {
            throw new RuntimeException("插入元素为空, 请检查参数");
        }
        String insertSql = getInsertPrepareSQL(obj);
        //log.debug(insertSql);
        try (Connection conn = ConnectionManagerUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            execUpdate(statement, obj);
            // 检索由于执行此 Statement 对象而创建的所有自动生成的键
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                log.error("插入数据，获取主键失败");
                throw new RuntimeException("无法获取到主键");
            }
        } catch (Exception e) {
            log.error("插入失败", e);
            throw new RuntimeException("数据库插入失败:" + e.getMessage());
        } finally {
            fieldDetailMapThreadLocal.remove();
        }

    }

    @Override
    public <T> int updateById(T obj) {
        if (obj == null) {
            throw new RuntimeException("插入元素为空, 请检查参数");
        }
        String updatePrepareSQL = getUpdatePrepareSQL(obj);
        //log.debug(updatePrepareSQL);
        try (Connection conn = ConnectionManagerUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(updatePrepareSQL)) {
            return execUpdate(statement, obj);
        } catch (Exception e) {
            log.error("插入失败", e);
            throw new RuntimeException("数据库插入失败:" + e.getMessage());
        } finally {
            fieldDetailMapThreadLocal.remove();
        }
    }


    private String getUpdatePrepareSQL(Object object) {

        Class<?> objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        List<String> columnList = new ArrayList<>(fields.length);

        int paramIndex = 1;
        Field idField = null;
        for (int i = 0; i < fields.length; i++) {
            try {
                // todo 后续改为通过getXXX方法获取列的值
                fields[i].setAccessible(true);
                if (fields[i].get(object) == null) {
                    continue;
                }
            } catch (IllegalAccessException e) {
                continue;
            }
            if (fields[i].getAnnotation(Id.class) == null) {
                String column = NameConvertUtil.toDbRule(fields[i].getName());
                columnList.add(NameConvertUtil.around(column, "`") + "= ?");
                recordFieldDetail(new FieldDetail(fields[i].getName(), paramIndex++, 0, fields[i].getClass()));
            } else {
                idField = fields[i];
            }

        }

        if (idField == null) {
            log.error("缺少id字段");
            throw new IllegalStateException("缺少id字段");
        }

        String id = NameConvertUtil.toDbRule(idField.getName());
        recordFieldDetail(new FieldDetail(idField.getName(), paramIndex, 0, idField.getClass()));

        String tableNameStr = NameConvertUtil.toDbRule(objectClass.getSimpleName());

        String columnStr = columnList.stream().collect(Collectors.joining(","));

        // 拼接预编译sql片段
        return String.format(UPDATE_TEMPLATE, tableNameStr, columnStr, id);
    }


    int execUpdate(PreparedStatement statement, Object obj) throws SQLException, IllegalAccessException {
        setParam(statement, obj);
        return statement.executeUpdate();
    }

    private void setParam(PreparedStatement statement, Object object) throws SQLException, IllegalAccessException {
        Map<String, FieldDetail> fieldDetailMap = fieldDetailMapThreadLocal.get();
        Class<?> objectClass = object.getClass();
        Field[] declaredFields = objectClass.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            FieldDetail fieldDetail = fieldDetailMap.get(fieldName);
            if (fieldDetail != null) {
                // todo 改为getxxx方法获取
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {
                    //log.debug("参数{} ==> [{}]", fieldDetail.getParamIndex(), value);
                    statement.setObject(fieldDetail.getParamIndex(), value);
                }
            }
        }
    }


    @Override
    public <T> int batchInsert(List<T> objectList) {
        if (objectList == null || objectList.isEmpty()) {
            log.warn("插入元素为空, 请检查参数");
            return 0;
        }
        try {
            return batchInsertData(objectList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> int batchInsertData(List<T> objectList) throws SQLException {
        Connection conn = ConnectionManagerUtil.getConnection();
        PreparedStatement statement = null;
        int successRow = 0;
        try {
            // 设置不自动提交事务
            conn.setAutoCommit(false);
            Iterator<T> iterator = objectList.iterator();
            while (iterator.hasNext()) {
                T obj = iterator.next();
                statement = conn.prepareStatement(getInsertPrepareSQL(obj));
                successRow += execUpdate(statement, obj);
            }
            // 批量插入完成，提交事务
            conn.commit();
        } catch (Exception e) {
            // 出现异常，事务回滚
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            log.error("批量插入发生异常，当前执行列表报错下标:{}", successRow + 1, e);
            throw new RuntimeException(e);
        } finally {
            fieldDetailMapThreadLocal.remove();

            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return successRow;
    }


    @Override
    public <T> int batchDeleteByIds(List<T> ids) {

        return 0;
    }

    @Override
    public <T> void createTable(Class<T> entity) {
        // 构造建表sql
        String createTableSql = getCreateTableSql(entity);
        log.info("待创建的表sql为:\n{}", createTableSql);
        // 执行sql
        exec(createTableSql);
        log.info("===> 建表成功");
    }

    @Override
    public <T> void createTableIfNotExist(Class<T> entity) {

        Connection connection = ConnectionManagerUtil.getConnection();
        try {
            String schema = ((ConnectionImpl) connection).getDatabase();
            String tableName = getTableName(entity);
            String querySql = "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE table_schema = '" + schema + "' and table_name ='" + tableName + "'";
            log.debug("执行sql==> {}", querySql);
            Long resultNum = getOne(querySql, Long.class);
            log.debug("结果==> {}", resultNum);
            // 如果表不存在，则创建表
            if (resultNum == 0L) {
                createTable(entity);
            } else {
                log.info("表{}已存在,无需执行建表语句", tableName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean exec(String sql) {
        try (Connection conn = ConnectionManagerUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            return statement.execute();
        } catch (Exception e) {
            log.error("执行失败", e);
            throw new RuntimeException("sql执行失败:" + e.getMessage());
        }
    }


    private String getCreateTableSql(Class<?> entity) {
        // 获取表名
        String tableName = getTableName(entity);

        Field[] fields = entity.getDeclaredFields();
        /*
         * 参数1：列名，参数2：列类型
         * 如：`username` varchar(20),
         */
        final String template = "`%s` %s,\n";
        StringBuilder fieldSql = new StringBuilder();
        String idFieldName = "";
        for (Field field : fields) {
            String databaseFieldName = null;

            String fieldType = null;

            // 解析TableField注解获取字段名、字段类型规则
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null) {
                databaseFieldName = "".equals(tableField.value()) ? NameConvertUtil.toDbRule(field.getName()) : tableField.value();
                fieldType = "".equals(tableField.type()) ? getDatabaseType(field.getType()) : tableField.type();
            } else {
                databaseFieldName = NameConvertUtil.toDbRule(field.getName());
                fieldType = getDatabaseType(field.getType());
            }

            // 如果是主键，设置自增
            if (Objects.nonNull(field.getAnnotation(Id.class))) {
                idFieldName = databaseFieldName;
                fieldType = fieldType + " NOT NULL AUTO_INCREMENT";
            }
            if (Objects.nonNull(field.getAnnotation(DateAuto.class))) {
                DateAuto annotation = field.getAnnotation(DateAuto.class);
                fieldType = fieldType + ("update".equals(annotation.value()) ?
                        " NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                        : " NOT NULL DEFAULT CURRENT_TIMESTAMP");
            }
            fieldSql.append(String.format(template, databaseFieldName, fieldType));
        }
        fieldSql.append("PRIMARY KEY (`" + idFieldName + "`)");

        // 组装完整的建表sql
        return String.format(CREATE_TABLE_SQL_TEMPLATE, tableName, fieldSql.toString());
    }

    /**
     * 获取获取表名
     *
     * @param entity
     * @return
     */
    private String getTableName(Class<?> entity) {
        return NameConvertUtil.toDbRule(entity.getSimpleName());
    }

    private String getDatabaseType(Class<?> type) {
        if (String.class == type) {
            return "varchar(255)";
        } else if (Integer.class == type) {
            return "int";
        } else if (Long.class == type) {
            return "bigint";
        } else if (Date.class == type) {
            return "datetime";
        }
        return "varchar(250)";
    }


    private String getInsertPrepareSQL(Object object) {

        Class<?> objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        List<String> columnList = new ArrayList<>(fields.length);

        int paramIndex = 1;
        for (int i = 0; i < fields.length; i++) {
            try {
                // todo 后续改为通过getXXX方法获取列的值
                fields[i].setAccessible(true);
                if (fields[i].get(object) == null) {
                    continue;
                }
            } catch (IllegalAccessException e) {
                continue;
            }
            String column = NameConvertUtil.toDbRule(fields[i].getName());
            columnList.add(NameConvertUtil.around(column, "`"));

            // 记录列参数信息到ThreadLocal
            recordFieldDetail(new FieldDetail(fields[i].getName(), paramIndex++, 0, fields[i].getClass()));
        }

        String tableNameStr = NameConvertUtil.toDbRule(objectClass.getSimpleName());

        String columnStr = columnList.stream().collect(Collectors.joining(","));

        // 拼接预编译sql片段
        return String.format(INSERT_TEMPLATE, tableNameStr, columnStr, getPlaceholder(paramIndex - 1));
    }

    private void recordFieldDetail(FieldDetail fieldDetail) {
        Map<String, FieldDetail> fieldDetailMap = fieldDetailMapThreadLocal.get();
        if (fieldDetailMap == null) {
            fieldDetailMap = new HashMap<>();
            fieldDetailMapThreadLocal.set(fieldDetailMap);
        }
        fieldDetailMap.put(fieldDetail.getFieldName(), fieldDetail);
    }


    /**
     * 获取占位符片段
     * 例如 ==> "?,?,?,?"
     *
     * @param num
     * @return
     */
    private String getPlaceholder(int num) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < num; i++) {
            str.append("?");
            if (i != num - 1) {
                str.append(",");
            }
        }
        return str.toString();
    }


    private Set<String> getTableColumnNameSet(ResultSet resultSet) throws SQLException {
        Set<String> fieldNameSet = new HashSet<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fieldNameSet.add(metaData.getColumnLabel(i));
        }
        return fieldNameSet;
    }

    /**
     * 获取数据库支持的映射类型
     *
     * @param clazz
     * @return
     */
    public static boolean isMappingSupportType(Class clazz) {
        return isBaseType(clazz) || supportTypeSet.contains(clazz);
    }

    /**
     * 判断是否是基础数据类型或者基础类型的包装类型
     *
     * @param clazz
     * @return
     */
    public static boolean isBaseType(Class clazz) {
        try {
            return clazz.isPrimitive() || ((Class) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }


}
