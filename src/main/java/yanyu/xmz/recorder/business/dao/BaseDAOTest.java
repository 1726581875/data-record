package yanyu.xmz.recorder.business.dao;

import yanyu.xmz.recorder.business.entity.metadata.MysqlTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/8/23
 */
public class BaseDAOTest {

    public static void main(String[] args) {


        BaseDAO.mysqlInstance().dropTableIfExist(TestObj.class);


        BaseDAO.mysqlInstance().createTableIfNotExist(TestObj.class);


        List<TestObj> objList = new ArrayList<>();
        objList.add(new TestObj("xiaoxiao", new Date(), new Date()));
        objList.add(new TestObj("xianxian", new Date(), new Date()));
        objList.add(new TestObj("lanlan", new Date(), new Date()));

        BaseDAO.mysqlInstance().batchInsert(objList);


        TestObj testObj = new TestObj("LL", new Date(), new Date());
        testObj.setId(1L);
        BaseDAO.mysqlInstance().updateById(testObj);

        List<TestObj> testObjList = BaseDAO.mysqlInstance()
                .getList("select * from test_obj", TestObj.class);
        testObjList.forEach(System.out::println);
    }


}
