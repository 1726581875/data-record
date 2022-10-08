package yanyu.xmz.recorder.business.service;

import yanyu.xmz.recorder.business.model.dto.DataMigrationDTO;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
public interface DataMigrationService {


    void doDataMigration(DataMigrationDTO dto);

}
