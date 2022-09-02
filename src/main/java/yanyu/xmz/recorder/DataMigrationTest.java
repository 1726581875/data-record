package yanyu.xmz.recorder;

import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2022/8/24
 */
public class DataMigrationTest {

    public static void main(String[] args) {
/*        ConnectUtil.changeDataSource("monitor");
        Map<String,Object> resultMap = BaseDAO.mysqlInstance().getOne("show create table mooc.course", Map.class);

        resultMap.forEach((k,v) -> System.out.println("key=" + k + ",v=" + v));

        ConnectUtil.changeDataSource("local");
        BaseDAO.mysqlInstance().exec((String) resultMap.get("Create Table"));*/


        ConnectUtil.changeDataSource("monitor");


        List<Map> resultMapList = BaseDAO.mysqlInstance().getList("select * from mooc.course limit 10", Map.class);
        for (Map<String,Object> rowMap : resultMapList){
            rowMap.forEach((k,v) -> System.out.println(k + "=" + v));
            System.out.println("===========");
        }


    }


}
