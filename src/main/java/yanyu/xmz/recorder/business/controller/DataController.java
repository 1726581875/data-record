package yanyu.xmz.recorder.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yanyu.xmz.recorder.business.model.RespResult;
import yanyu.xmz.recorder.business.model.vo.DataListVO;
import yanyu.xmz.recorder.business.service.DataService;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/9/14
 */
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/getList")
    public RespResult<List<List>> getDataList(String tableName, Integer offset, Integer limit) {
        return RespResult.success(dataService.getDataList(tableName, offset, limit));
    }

    @GetMapping("/count")
    public RespResult<Long> count(String tableName) {
        return  RespResult.success(dataService.count(tableName));
    }

}
