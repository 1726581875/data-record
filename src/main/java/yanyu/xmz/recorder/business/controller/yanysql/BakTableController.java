package yanyu.xmz.recorder.business.controller.yanysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yanyu.xmz.recorder.business.entity.yanysql.BakTable;
import yanyu.xmz.recorder.business.model.RespResult;
import yanyu.xmz.recorder.business.service.yanysql.BakTableService;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2023/10/21
 */
@RestController
@RequestMapping("/bakTable")
public class BakTableController {

    @Autowired
    private BakTableService bakTableService;

    @GetMapping("/getBakTableList")
    private RespResult<List<BakTable>> getBakTableList(@RequestParam("tenantId") String tenantId,
                                                       @RequestParam(name = "tableName", defaultValue = "") String tableName){
        return RespResult.success(bakTableService.getBakTableList(tenantId, tableName));
    }


    @GetMapping("/getDataList")
    public RespResult<List<List>> getDataList(String tableName, Integer offset, Integer limit) {
        return RespResult.success(bakTableService.getDataList(tableName, offset, limit));
    }

    @GetMapping("/getDataCount")
    public RespResult<Long> count(String tableName) {
        return RespResult.success(bakTableService.count(tableName));
    }


}
