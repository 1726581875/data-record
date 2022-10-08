package yanyu.xmz.recorder.business.service;

import yanyu.xmz.recorder.business.model.entity.SysDataSource;

/**
 * @author xiaomingzhang
 * @date 2022/10/8
 */
public interface DataSourceService {


    SysDataSource getDataSource(String tenantId, Long dataSourceId);

}
