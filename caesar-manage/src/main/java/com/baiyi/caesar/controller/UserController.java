package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.param.user.UserParam;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.user.UserPermissionFacade;
import com.baiyi.caesar.domain.vo.server.ServerTreeVO;
import com.baiyi.caesar.domain.vo.user.UserPermissionVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:33 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserPermissionFacade permissionFacade;

    @ApiOperation(value = "分页查询用户列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.User>> queryUserPage(@RequestBody @Valid UserParam.UserPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserPage(pageQuery));
    }

    @ApiOperation(value = "新增用户")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServer(@RequestBody @Valid UserVO.User user) {
        userFacade.addUser(user);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新用户")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServer(@RequestBody @Valid UserVO.User user) {
        userFacade.updateUser(user);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "解除用户业务许可")
    @PutMapping(value = "/business/permission/revoke", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserBusinessPermission(@RequestBody @Valid UserPermissionVO.UserBusinessPermission userBusinessPermission) {
        permissionFacade.revokeUserBusinessPermission(userBusinessPermission);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "授予用户业务许可")
    @PostMapping(value = "/business/permission/grant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserBusinessPermission(@RequestBody @Valid UserPermissionVO.UserBusinessPermission userBusinessPermission) {
        permissionFacade.grantUserBusinessPermission(userBusinessPermission);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "设置用户业务许可（角色）")
    @PutMapping(value = "/business/permission/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setUserBusinessPermission(@RequestParam @Valid int id) {
        permissionFacade.settUserBusinessPermission(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询用户的服务器树")
    @PostMapping(value = "/server/tree/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ServerTreeVO.ServerTree> queryUserServerTree(@RequestBody @Valid ServerGroupParam.UserServerTreeQuery queryParam) {
        return new HttpResult<>(userFacade.queryUserServerTree(queryParam));
    }

}
