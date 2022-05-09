package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.ServerFacade;
import com.baiyi.opscloud.util.OptionsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/5/24 5:41 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/server")
@Api(tags = "服务器管理")
@RequiredArgsConstructor
public class ServerController {

    private final ServerFacade serverFacade;

    @ApiOperation(value = "查询服务器协议选项")
    @GetMapping(value = "/protocol/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getServerProtocolOptions() {
        return new HttpResult<>(OptionsUtil.toProtocolOptions());
    }

    @ApiOperation(value = "分页查询服务器列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerVO.Server>> queryServerPage(@RequestBody @Valid ServerParam.ServerPageQuery pageQuery) {
        return new HttpResult<>(serverFacade.queryServerPage(pageQuery));
    }

    @ApiOperation(value = "新增服务器")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ServerVO.Server> addServer(@RequestBody @Valid ServerVO.Server server) {
        return new HttpResult<>(serverFacade.addServer(server));
    }

    @ApiOperation(value = "更新服务器")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServer(@RequestBody @Valid ServerVO.Server server) {
        serverFacade.updateServer(server);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "扫描服务器监控状态")
    @PutMapping(value = "/monitor/scan",  produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanServerMonitorStatus() {
        serverFacade.scanServerMonitoringStatus();
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的服务器")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerById(@RequestParam int id) {
        serverFacade.deleteServerById(id);
        return HttpResult.SUCCESS;
    }
}
