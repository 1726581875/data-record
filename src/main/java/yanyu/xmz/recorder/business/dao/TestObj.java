package yanyu.xmz.recorder.business.dao;

import yanyu.xmz.recorder.business.dao.annotation.DateAuto;
import yanyu.xmz.recorder.business.dao.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public TestObj(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public TestObj(String name, Date createTime, Date updateTime) {
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }




    @Override
    public String toString() {
        return "TestObj{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public static void main(String[] args) {
        BaseDAO.mysqlInstance().dropTableIfExist(TestObj.class);

        BaseDAO.mysqlInstance().createTableIfNotExist(TestObj.class);

        List<List> list = BaseDAO.mysqlInstance().getList("select * from test_obj", List.class);

        list.forEach(e -> System.out.println(e.stream().collect(Collectors.joining(","))));

    }

}
