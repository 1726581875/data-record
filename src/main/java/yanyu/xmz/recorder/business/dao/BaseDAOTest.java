package yanyu.xmz.recorder.business.dao;

import yanyu.xmz.recorder.business.entity.metadata.MysqlTable;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/8/23
 */
public class BaseDAOTest {

    public static void main(String[] args) {
        List<MysqlTable> tableList = BaseDAO.mysqlInstance()
                .getList("select * from mysql_table where table_schema=?", MysqlTable.class, "mooc");

        tableList.forEach(System.out::println);
    }


}
