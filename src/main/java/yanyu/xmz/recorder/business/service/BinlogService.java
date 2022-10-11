package yanyu.xmz.recorder.business.service;

/**
 * @author xiaomingzhang
 * @date 2022/10/11
 */
public interface BinlogService {

    void listenBinlog(String tenantId, Long dataSourceId);

}
