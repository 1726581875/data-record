package yanyu.xmz.recorder.business.service;

import yanyu.xmz.recorder.business.model.RespResult;

/**
 * @author xiaomingzhang
 * @date 2022/10/11
 */
public interface BinlogService {

    RespResult listenBinlog(String tenantId, Long dataSourceId);

    RespResult cancelListen(String tenantId, Long dataSourceId);

}
