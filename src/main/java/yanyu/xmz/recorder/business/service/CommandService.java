package yanyu.xmz.recorder.business.service;

import yanyu.xmz.recorder.business.model.dto.TenantDataSourceDTO;

/**
 * @author xiaomingzhang
 * @date 2022/10/9
 */
public interface CommandService {

    boolean ping(TenantDataSourceDTO dto);

}
