package yanyu.xmz.recorder.business.service.yanysql;

import yanyu.xmz.recorder.business.entity.yanysql.BakTable;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2023/10/21
 */
public interface BakTableService {

    List<BakTable> getBakTableList(String tenantId, String tableName);

    List<List> getDataList(String tableName, Integer offset, Integer limit);

    Long count(String tableName);

}
