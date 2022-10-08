package yanyu.xmz.recorder.business.service.impl;

import org.springframework.stereotype.Service;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.model.dto.DataExportDTO;
import yanyu.xmz.recorder.business.service.DataService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
@Service
public class DataServiceImpl implements DataService {


    @Override
    public List<List> getDataList(String tableName, Integer offset, Integer limit) {

        List<List> columnList = BaseDAO.mysqlInstance().getList("select * from `" + tableName
                + "` limit " + limit + " offset " + offset , List.class);
        return columnList;
    }

    @Override
    public Long count(String tableName) {
        return BaseDAO.mysqlInstance().getOne("select count(*) from `" + tableName + "`", Long.class);
    }

    @Override
    public void export(DataExportDTO exportDTO, HttpServletResponse response) {

    }
}
