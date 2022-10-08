package yanyu.xmz.recorder.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yanyu.xmz.recorder.business.model.RespResult;
import yanyu.xmz.recorder.business.model.dto.DataMigrationDTO;
import yanyu.xmz.recorder.business.service.DataMigrationService;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
@RestController
@RequestMapping("/dm")
public class DataMigrationController {

    @Autowired
    private DataMigrationService dataMigrationService;

    @PostMapping("/do")
    public RespResult doDataMigration(@RequestBody DataMigrationDTO dto) {
        dataMigrationService.doDataMigration(dto);
        return RespResult.success();
    }


}
