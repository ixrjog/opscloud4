package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerAccountParam;
import com.baiyi.opscloud.domain.vo.server.ServerAccountVO;
import com.baiyi.opscloud.facade.server.ServerAccountFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:24 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/server/account")
@Api(tags = "服务器账户管理")
@RequiredArgsConstructor
public class ServerAccountController {

    private final ServerAccountFacade serverAccountFacade;

    @ApiOperation(value = "更新服务器账户授权")
    @PutMapping(value = "/permission/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerAccountPermission(@RequestBody @Valid ServerAccountParam.UpdateServerAccountPermission updatePermission) {
        serverAccountFacade.updateServerAccountPermission(updatePermission);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询服务器账户列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerAccountVO.Account>> queryServerAccountPage(@RequestBody @Valid ServerAccountParam.ServerAccountPageQuery pageQuery) {
        return new HttpResult<>(serverAccountFacade.queryServerAccountPage(pageQuery));
    }

    @ApiOperation(value = "新增服务器账户")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServerAccount(@RequestBody @Valid ServerAccountVO.Account account) {
        serverAccountFacade.addServerAccount(account);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新服务器账户")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerAccount(@RequestBody @Valid ServerAccountVO.Account account) {
        serverAccountFacade.updateServerAccount(account);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的服务器账户")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerAccountById(@RequestParam int id) {
        serverAccountFacade.deleteServerAccountById(id);
        return HttpResult.SUCCESS;
    }

}
