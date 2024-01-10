package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.ServerFacade;
import com.baiyi.opscloud.util.OptionsUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/5/24 5:41 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/server")
@Tag(name = "服务器管理")
@RequiredArgsConstructor
public class ServerController {

    private final ServerFacade serverFacade;

    @Operation(summary = "查询服务器协议选项")
    @GetMapping(value = "/protocol/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getServerProtocolOptions() {
        return new HttpResult<>(OptionsUtil.toProtocolOptions());
    }

    @Operation(summary = "分页查询服务器列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerVO.Server>> queryServerPage(@RequestBody @Valid ServerParam.ServerPageQuery pageQuery) {
        return new HttpResult<>(serverFacade.queryServerPage(pageQuery));
    }

    @Operation(summary = "新增服务器")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ServerVO.Server> addServer(@RequestBody @Valid ServerParam.AddServer server) {
        return new HttpResult<>(serverFacade.addServer(server));
    }

    @Operation(summary = "更新服务器")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServer(@RequestBody @Valid ServerParam.UpdateServer server) {
        serverFacade.updateServer(server);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "扫描服务器监控状态")
    @PutMapping(value = "/monitor/scan", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanServerMonitorStatus() {
        serverFacade.scanServerMonitoringStatus();
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的服务器")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerById(@RequestParam int id) {
        serverFacade.deleteServerById(id);
        return HttpResult.SUCCESS;
    }

}