package yanyu.xmz.recorder.business.service.yanysql.impl;

import org.springframework.stereotype.Service;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.dao.YanySqlBaseDAO;
import yanyu.xmz.recorder.business.dao.util.PropertiesReaderUtil;
import yanyu.xmz.recorder.business.entity.yanysql.BakTable;
import yanyu.xmz.recorder.business.service.yanysql.BakTableService;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2023/10/21
 */
@Service
public class BakTableServiceImpl implements BakTableService {

    private YanySqlBaseDAO bakYanyDatabaseDAO = new YanySqlBaseDAO(PropertiesReaderUtil.get("yanysql.data_bak.url"));

    @Override
    public List<BakTable> getBakTableList(String tenantId, String tableName) {
        String path = "_" + tenantId + "_";
        String sql = "select * from bak_table where path like '" + path + "%' and table_name like '%" + tableName + "%' order by create_time desc";
        List<BakTable> list = bakYanyDatabaseDAO.getList(sql, BakTable.class);
        return list;
    }

    @Override
    public List<List> getDataList(String tableName, Integer offset, Integer limit) {
        List<List> columnList = bakYanyDatabaseDAO.getList("select * from " + tableName
                + " limit " + limit + " offset " + offset, List.class);
        return columnList;
    }

    @Override
    public Long count(String tableName) {
        return bakYanyDatabaseDAO.getOne("select count(*) from " + tableName + "", Long.class);
    }
}
