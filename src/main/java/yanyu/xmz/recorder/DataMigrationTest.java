package yanyu.xmz.recorder;

import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.util.ConnectUtil;

import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2022/8/24
 */
public class DataMigrationTest {

    public static void main(String[] args) {
        ConnectUtil.changeDataSource("monitor");
        Map<String,Object> resultMap = BaseDAO.mysqlInstance().getOne("show create table mooc.course", Map.class);
        resultMap.forEach((k,v) -> System.out.println("key=" + k + ",v=" + v));
    }


}
