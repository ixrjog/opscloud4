package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;
import com.baiyi.opscloud.facade.ServerTaskFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:19 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/server/task")
@Api(tags = "服务器管理")
public class ServerTaskController {

    @Resource
    private ServerTaskFacade serverTaskFacade;

    @ApiOperation(value = "批量命令")
    @PostMapping(value = "/command/executor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessWrapper<Boolean>> executorCommand(@RequestBody @Valid ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor) {
        return new HttpResult(serverTaskFacade.executorCommand(serverTaskCommandExecutor));
    }

    @ApiOperation(value = "查询任务")
    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OcServerTaskVO.ServerTask> queryServerTask(@Valid int taskId) {
        return new HttpResult<>(serverTaskFacade.queryServerTaskByTaskId(taskId));
    }

    @ApiOperation(value = "创建ansible主机配置文件")
    @GetMapping(value = "/ansible/hosts/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createAnsibleHosts() {
        return new HttpResult<>(serverTaskFacade.createAnsibleHosts());
    }

}
