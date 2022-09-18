package yanyu.xmz.recorder.business.service;

import yanyu.xmz.recorder.business.model.vo.DataListVO;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
public interface DataService {


    List<List> getDataList(String tableName, Integer offset, Integer limit);

    Long count(String tableName);

}
