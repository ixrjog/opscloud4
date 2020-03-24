package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.user.OcUserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
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
    @PostMapping(value = "/token/apply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> applyUserApiToken(@RequestBody @Valid OcUserApiTokenVO.UserApiToken userApiToken) {
        return new HttpResult<>(userFacade.applyUserApiToken(userApiToken));
    }

    @ApiOperation(value = "用户删除ApiToken")
    @DeleteMapping(value = "/token/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delUserApiToken(@RequestParam int id) {
        return new HttpResult<>(userFacade.delUserApiToken(id));
    }

    @ApiOperation(value = "用户保存凭据")
    @PostMapping(value = "/credential/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveUserCredential(@RequestBody @Valid OcUserCredentialVO.UserCredential userCredential) {
        return new HttpResult<>(userFacade.saveUserCredentia(userCredential));
    }

    @ApiOperation(value = "更新user信息")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUser(@RequestBody @Valid OcUserVO.User user) {
        return new HttpResult<>(userFacade.updateBaseUser(user));
    }

    @ApiOperation(value = "创建user")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createUser(@RequestBody @Valid OcUserVO.User user) {
        return new HttpResult<>(userFacade.createUser(user));
    }

    @ApiOperation(value = "同步user")
    @GetMapping(value = "/ldap/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUser() {
        return new HttpResult<>(userFacade.syncUser());
    }

    @ApiOperation(value = "分页查询user授权的用户组列表")
    @GetMapping(value = "/include/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcUserGroupVO.UserGroup>> queryUserIncludeUserGroupPage(@Valid UserGroupParam.UserUserGroupPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserIncludeUserGroupPage(pageQuery));
    }

    @ApiOperation(value = "分页查询user未授权的用户组列表")
    @GetMapping(value = "/exclude/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcUserGroupVO.UserGroup>> queryUserExcludeUserGroupPage(@Valid UserGroupParam.UserUserGroupPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserExcludeUserGroupPage(pageQuery));
    }

    @ApiOperation(value = "分页查询用户组列表")
    @GetMapping(value = "/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcUserGroupVO.UserGroup>> queryUserGroupPage(@Valid UserGroupParam.PageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserGroupPage(pageQuery));
    }

    // user group
    @ApiOperation(value = "用户组授权给用户")
    @GetMapping(value = "/group/grant", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserUserGroup(@Valid UserGroupParam.UserUserGroupPermission userUserGroupPermission) {
        return new HttpResult<>(userFacade.grantUserUserGroup(userUserGroupPermission));
    }

    @ApiOperation(value = "用户解除用户组授权")
    @DeleteMapping(value = "/group/revoke", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserUserGroup(@Valid UserGroupParam.UserUserGroupPermission userUserGroupPermission) {
        return new HttpResult<>(userFacade.revokeUserUserGroup(userUserGroupPermission));
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
