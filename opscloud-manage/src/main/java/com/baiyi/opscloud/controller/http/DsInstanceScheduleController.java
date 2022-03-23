package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.datasource.ScheduleVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceScheduleFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/3/23 15:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/datasource/instance/schedule")
@Api(tags = "数据源实例任务")
@RequiredArgsConstructor
public class DsInstanceScheduleController {

    private final DsInstanceScheduleFacade scheduleFacade;

    @ApiOperation(value = "查询指定ID的数据源实例任务")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ScheduleVO.Job>> queryScheduleById(@RequestParam int id) {
        return new HttpResult<>(scheduleFacade.queryJob(id));
    }

}
