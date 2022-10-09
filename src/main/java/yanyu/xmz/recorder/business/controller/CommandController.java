package yanyu.xmz.recorder.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yanyu.xmz.recorder.business.model.RespResult;
import yanyu.xmz.recorder.business.model.dto.TenantDataSourceDTO;
import yanyu.xmz.recorder.business.service.CommandService;

/**
 * @author xiaomingzhang
 * @date 2022/10/9
 */
@RestController
@RequestMapping("/command")
public class CommandController {

    @Autowired
    private CommandService commandService;


    @PostMapping("ping")
    public RespResult<Boolean> ping(@RequestBody TenantDataSourceDTO dto){
        return RespResult.success(commandService.ping(dto));
    }



}
