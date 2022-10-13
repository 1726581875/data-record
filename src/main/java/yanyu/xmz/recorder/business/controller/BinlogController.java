package yanyu.xmz.recorder.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yanyu.xmz.recorder.business.model.RespResult;
import yanyu.xmz.recorder.business.model.dto.TenantDataSourceDTO;
import yanyu.xmz.recorder.business.service.BinlogService;

/**
 * @author xiaomingzhang
 * @date 2022/10/11
 */
@RestController
@RequestMapping("/binlog")
public class BinlogController {

    @Autowired
    private BinlogService binlogService;


    @PostMapping("/listenBinlog")
    public RespResult listenBinlog(@RequestBody TenantDataSourceDTO dto){
        binlogService.listenBinlog(dto.getTenantId(), dto.getDataSourceId());
        return RespResult.success();
    }




}
