package yanyu.xmz.recorder.business.dao;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/6/13
 */
public class TestObj {

    @Id
    private Long id;

    private String name;

    @DateAuto
    private Date createTime;

    @DateAuto("update")
    private Date updateTime;

    public TestObj(String name, Date createTime, Date updateTime) {
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public static void main(String[] args) {
        BaseDAO.mysqlInstance().dropTableIfExist(TestObj.class);

        BaseDAO.mysqlInstance().createTableIfNotExist(TestObj.class);

        List<TestObj> testObjList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            testObjList.add(new TestObj("xmz",null, null));
        }

        BaseDAO.mysqlInstance().batchInsert(testObjList);

    }

}
