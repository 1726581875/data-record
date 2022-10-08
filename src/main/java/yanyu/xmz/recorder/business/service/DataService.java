package yanyu.xmz.recorder.business.service;

import yanyu.xmz.recorder.business.model.dto.DataExportDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
public interface DataService {


    List<List> getDataList(String tableName, Integer offset, Integer limit);

    Long count(String tableName);

    void export(DataExportDTO exportDTO, HttpServletResponse response);

}
