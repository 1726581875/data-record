package yanyu.xmz.recorder.business.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/12/29
 */
public interface BaseDAO {

    /**
     * 获取查询数据结果列表
     * sql注入风险
     * @param sql
     * @param returnType
     * @param <T>
     * @return
     */
    <T> List<T> getList(String sql, Class<T> returnType);

    /**
     * 获取数据列表
     * @param sql 预编译sql
     * @param returnType 返回类型
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> getList(String sql, Class<T> returnType, Object ... params);

    /**
     * 获取一条记录
     * @param sql
     * @param returnType
     * @param <T>
     * @return
     */
    <T> T getOne(String sql, Class<T> returnType);

    /**
     * 获取一条记录
     * @param sql
     * @param returnType
     * @param <T>
     * @return
     */
    <T> T getOne(String sql, Class<T> returnType, Object ... params);

    /**
     * 插入单条数据
     * @param obj
     * @param <T>
     * @return
     */
    <T> int insert(T obj);

    /**
     * 插入数据，返回自增主键
     * @param obj
     * @param <T>
     * @return
     */
    <T> Long insertReturnKey(T obj);

    /**
     * 根据id更新
     * @param obj
     * @param <T>
     * @return
     */
    <T> int updateById(T obj);

    /**
     * 批量插入
     * todo 2022/06/13 貌似还有点问题
     * @param objectList
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> int batchInsert(List<T> objectList);


    <T> int batchInsert(String tableName, List<T> columnValueMap);

    /**
     * 批量删除
     * @param ids
     * @param <T>
     * @return
     */
    <T> int batchDeleteByIds(List<T> ids);

    /**
     * 根据id删除
     * @param entity
     * @param id
     * @param <T>
     * @return
     */
    <T> boolean deleteById(Class<T> entity, Object id);


    /**
     * 创建表
     * @param entity
     * @param <T>
     */
    <T> void createTable(Class<T> entity);

    /**
     * 如果表存在则删除表
     * @param entity
     * @param <T>
     */
    <T> void dropTableIfExist(Class<T> entity);
    <T> void dropTableIfExist(Class<T> entity, String suffix);

    /**
     * 如果表不存在则创建表
     * @param entity
     * @param <T>
     */
    <T> void createTableIfNotExist(Class<T> entity);
    <T> void createTableIfNotExist(Class<T> entity, String suffix);


    /**
     * 执行sql
     * @param sql
     * @return
     */
    boolean exec(String sql);

    /**
     * 获取一个实例
     * @return
     */
    static MysqlBaseDAO mysqlInstance() {
        return new MysqlBaseDAO();
    }


}
