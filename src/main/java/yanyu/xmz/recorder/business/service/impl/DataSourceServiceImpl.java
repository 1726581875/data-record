package yanyu.xmz.recorder.business.service.impl;

import org.springframework.stereotype.Service;
import yanyu.xmz.recorder.business.dao.BaseDAO;
import yanyu.xmz.recorder.business.model.entity.SysDataSource;
import yanyu.xmz.recorder.business.service.DataSourceService;

/**
 * @author xiaomingzhang
 * @date 2022/10/8
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {



    @Override
    public SysDataSource getDataSource(String tenantId, Long dataSourceId) {
        BaseDAO baseDAO = BaseDAO.mysqlInstance();
        SysDataSource dataSource = baseDAO.getOne("select * from sys_data_source where tenant_id = ? and id = ?",
                SysDataSource.class, tenantId, dataSourceId);

        if(dataSource == null) {
            throw new RuntimeException("租户数据源不存在,tenantId=" + tenantId + ",dataSourceId=" + dataSourceId);
        }
        return dataSource;
    }


}
