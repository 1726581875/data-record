package yanyu.xmz.recorder.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/12/29
 */
public interface BaseDAO {

    /**
     * 获取查询数据结果列表
     * @param sql
     * @param type
     * @param <T>
     * @return
     */
    <T> List<T> getList(String sql, Class<T> type);

    /**
     * 获取查询结果
     * @param sql
     * @param type
     * @param <T>
     * @return
     */
    <T> T getOne(String sql, Class<T> type);

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

    /**
     * 批量删除
     * @param ids
     * @param <T>
     * @return
     */
    <T> int batchDeleteByIds(List<T> ids);

    /**
     * 创建表
     * @param entity
     * @param <T>
     */
    <T> void createTable(Class<T> entity);

    /**
     * 创建表
     * @param entity
     * @param <T>
     */
    <T> void createTableIfNotExist(Class<T> entity);


    /**
     * 获取一个实例
     * @return
     */
    static MysqlBaseDAO mysqlInstance() {
        return new MysqlBaseDAO();
    }


}
