package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.user.OcUserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.OcUserGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.facade.UserFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:09 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Resource
    private UserFacade userFacade;

    // user

    /**
     * 管理员查询
     *
     * @param pageQuery
     * @return
     */
    @ApiOperation(value = "分页查询user列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcUserVO.User>> queryUserPage(@Valid UserParam.PageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserPage(pageQuery));
    }

    @ApiOperation(value = "查询user详情")
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OcUserVO.User> queryUserDetail() {
        return new HttpResult<>(userFacade.queryUserDetail());
    }

    @ApiOperation(value = "分页查询user列表")
    @PostMapping(value = "/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcUserVO.User>> fuzzyQueryUserPage(@RequestBody @Valid UserParam.PageQuery pageQuery) {
        return new HttpResult<>(userFacade.fuzzyQueryUserPage(pageQuery));
    }

    @ApiOperation(value = "获取一个随机密码")
    @GetMapping(value = "/password/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> queryUserRandomPassword() {
        return new HttpResult<>(userFacade.getRandomPassword());
    }

    @ApiOperation(value = "用户申请ApiToken")
    @PostMapping(value = "/apply/token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OcUserApiTokenVO.UserApiToken> applyUserApiToken(@RequestBody @Valid  OcUserApiTokenVO.UserApiToken userApiToken) {
        return new HttpResult<>(userFacade.applyUserApiToken(userApiToken));
    }

    @ApiOperation(value = "更新user信息")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUser(@RequestBody @Valid OcUserVO.User user) {
        return new HttpResult<>(userFacade.updateBaseUser(user));
    }

    @ApiOperation(value = "同步user")
    @GetMapping(value = "/ldap/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUser() {
        return new HttpResult<>(userFacade.syncUser());
    }

    // user group
    @ApiOperation(value = "分页查询user group列表")
    @GetMapping(value = "/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcUserGroupVO.UserGroup>> queryUserGroupPage(@Valid UserGroupParam.PageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserGroupPage(pageQuery));
    }

    @ApiOperation(value = "新增user group")
    @PostMapping(value = "/group/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTag(@RequestBody @Valid OcUserGroupVO.UserGroup userGroup) {
        return new HttpResult<>(userFacade.addUserGroup(userGroup));
    }

    @ApiOperation(value = "同步user group")
    @GetMapping(value = "/group/ldap/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUserGroup() {
        return new HttpResult<>(userFacade.syncUserGroup());
    }

}
