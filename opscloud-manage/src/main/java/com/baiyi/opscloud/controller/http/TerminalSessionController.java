package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionInstanceCommandParam;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionParam;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionInstanceCommandVO;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionVO;
import com.baiyi.opscloud.facade.sys.TerminalSessionFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:26 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/terminal/session")
@Api(tags = "终端会话审计")
@RequiredArgsConstructor
public class TerminalSessionController {

    private final TerminalSessionFacade terminalSessionFacade;

    @ApiOperation(value = "分页查询终端会话列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TerminalSessionVO.Session>> queryTerminalSessionPage(@RequestBody @Valid TerminalSessionParam.TerminalSessionPageQuery pageQuery) {
        return new HttpResult<>(terminalSessionFacade.queryTerminalSessionPage(pageQuery));
    }

    @ApiOperation(value = "分页查询终端会话命令列表")
    @PostMapping(value = "/instance/command/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TerminalSessionInstanceCommandVO.Command>> queryTerminalSessionCommandPage(@RequestBody @Valid TerminalSessionInstanceCommandParam.InstanceCommandPageQuery pageQuery) {
        return new HttpResult<>(terminalSessionFacade.queryTerminalSessionCommandPage(pageQuery));
    }

}
