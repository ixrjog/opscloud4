package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceScheduleParam;
import com.baiyi.opscloud.domain.vo.datasource.ScheduleVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceScheduleFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/3/23 15:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/datasource/instance/schedule")
@Tag(name = "数据源实例任务")
@RequiredArgsConstructor
public class DsInstanceScheduleController {

    private final DsInstanceScheduleFacade scheduleFacade;

    @Operation(summary = "查询指定ID的数据源实例任务")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ScheduleVO.Job>> queryScheduleById(@RequestParam int id) {
        return new HttpResult<>(scheduleFacade.queryJob(id));
    }

    @Operation(summary = "新增数据源实例任务")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addSchedule(@RequestBody @Valid DsInstanceScheduleParam.AddJob addJob) {
        scheduleFacade.addJob(addJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "暂停数据源实例任务")
    @PostMapping(value = "/pause", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pauseSchedule(@RequestBody @Valid DsInstanceScheduleParam.UpdateJob updateJob) {
        scheduleFacade.pauseJob(updateJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "恢复数据源实例任务")
    @PostMapping(value = "/resume", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> resumeSchedule(@RequestBody @Valid DsInstanceScheduleParam.UpdateJob updateJob) {
        scheduleFacade.resumeJob(updateJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除数据源实例任务")
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteSchedule(@RequestBody @Valid DsInstanceScheduleParam.DeleteJob deleteJob) {
        scheduleFacade.deleteJob(deleteJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "检查Cron表达式")
    @PostMapping(value = "/cron/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> checkCron(@RequestBody @Valid DsInstanceScheduleParam.CheckCron checkCron) {
        return new HttpResult<>(scheduleFacade.checkCron(checkCron));
    }

}