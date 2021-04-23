package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.param.user.UserServerTreeParam;
import com.baiyi.opscloud.domain.param.user.UserSettingParam;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.user.UserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.facade.UserSettingFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @Resource
    private UserSettingFacade userSettingFacade;

    /**
     * 管理员查询
     *
     * @param pageQuery
     * @return
     */
    @ApiOperation(value = "分页查询user列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.User>> queryUserPage(@Valid UserParam.UserPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserPage(pageQuery));
    }

    @ApiOperation(value = "分页模糊查询用户列表")
    @PostMapping(value = "/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.User>> fuzzyQueryUserPage(@RequestBody @Valid UserParam.UserPageQuery pageQuery) {
        return new HttpResult<>(userFacade.fuzzyQueryUserPage(pageQuery));
    }

    @ApiOperation(value = "查询user详情")
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.User> queryUserDetail() {
        return new HttpResult<>(userFacade.queryUserDetail());
    }

    @ApiOperation(value = "按用户名查询user详情")
    @GetMapping(value = "/detail/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.User> queryUserDetailByUsername(@Valid String username) {
        return new HttpResult<>(userFacade.queryUserDetailByUsername(username));
    }

    @ApiOperation(value = "离职")
    @PutMapping(value = "/retire", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> retireUser(@RequestParam @Valid int id) {
        return new HttpResult<>(userFacade.retireUser(id));
    }

    @ApiOperation(value = "复职")
    @PutMapping(value = "/reinstate", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> beReinstatedUser(@RequestParam @Valid int id) {
        return new HttpResult<>(userFacade.beReinstatedUser(id));
    }

    @ApiOperation(value = "获取一个随机密码")
    @GetMapping(value = "/password/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> queryUserRandomPassword() {
        return new HttpResult<>(userFacade.getRandomPassword());
    }

    @ApiOperation(value = "用户申请ApiToken")
    @PostMapping(value = "/token/apply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserApiTokenVO.UserApiToken> applyUserApiToken(@RequestBody @Valid UserApiTokenVO.UserApiToken userApiToken) {
        return new HttpResult<>(userFacade.applyUserApiToken(userApiToken));
    }

    @ApiOperation(value = "用户删除ApiToken")
    @DeleteMapping(value = "/token/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delUserApiToken(@RequestParam int id) {
        return new HttpResult<>(userFacade.delUserApiToken(id));
    }

    @ApiOperation(value = "用户保存凭据")
    @PostMapping(value = "/credential/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserCredentialVO.UserCredential> saveUserCredential(@RequestBody @Valid UserCredentialVO.UserCredential userCredential) {
        return new HttpResult<>(userFacade.saveUserCredentia(userCredential));
    }

    @ApiOperation(value = "更新用户信息")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUser(@RequestBody @Validated UserParam.UpdateUser updateUser) {
        return new HttpResult<>(userFacade.updateBaseUser(updateUser));
    }

    @ApiOperation(value = "创建用户")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createUser(@RequestBody @Validated UserParam.CreateUser createUser) {
        return new HttpResult<>(userFacade.createUser(createUser));
    }

    @ApiOperation(value = "用户名校验")
    @GetMapping(value = "/name/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> checkUsername(@RequestParam String username) {
        return new HttpResult<>(userFacade.checkUsername(username));
    }

    @ApiOperation(value = "同步用户")
    @GetMapping(value = "/ldap/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUser() {
        return new HttpResult<>(userFacade.syncUser());
    }

    @ApiOperation(value = "分页查询用户授权的用户组列表")
    @GetMapping(value = "/include/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserGroupVO.UserGroup>> queryUserIncludeUserGroupPage(@Valid UserBusinessGroupParam.UserUserGroupPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserIncludeUserGroupPage(pageQuery));
    }

    @ApiOperation(value = "分页查询用户未授权的用户组列表")
    @GetMapping(value = "/exclude/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserGroupVO.UserGroup>> queryUserExcludeUserGroupPage(@Valid UserBusinessGroupParam.UserUserGroupPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserExcludeUserGroupPage(pageQuery));
    }

    @ApiOperation(value = "分页查询用户组列表")
    @GetMapping(value = "/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserGroupVO.UserGroup>> queryUserGroupPage(@Valid UserBusinessGroupParam.PageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserGroupPage(pageQuery));
    }

    // user group
    @ApiOperation(value = "用户组授权给用户")
    @GetMapping(value = "/group/grant", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserUserGroup(@Valid UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission) {
        return new HttpResult<>(userFacade.grantUserUserGroup(userUserGroupPermission));
    }

    @ApiOperation(value = "用户解除用户组授权")
    @DeleteMapping(value = "/group/revoke", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserUserGroup(@Valid UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission) {
        return new HttpResult<>(userFacade.revokeUserUserGroup(userUserGroupPermission));
    }

    @ApiOperation(value = "新增用户组")
    @PostMapping(value = "/group/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addUserGroup(@RequestBody @Validated UserGroupVO.UserGroup userGroup) {
        return new HttpResult<>(userFacade.addUserGroup(userGroup));
    }

    @ApiOperation(value = "删除用户组")
    @DeleteMapping(value = "/group/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delUserGroup(@RequestParam @Valid int id) {
        return new HttpResult<>(userFacade.delUserGroupById(id));
    }

    @ApiOperation(value = "更新用户组")
    @PutMapping(value = "/group/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUserGroup(@RequestBody @Validated UserGroupVO.UserGroup userGroup) {
        return new HttpResult<>(userFacade.updateUserGroup(userGroup));
    }

    @ApiOperation(value = "同步用户组")
    @GetMapping(value = "/group/ldap/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUserGroup() {
        return new HttpResult<>(userFacade.syncUserGroup());
    }

    @ApiOperation(value = "查询用户授权的服务器树")
    @PostMapping(value = "/server/tree/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ServerTreeVO.MyServerTree> queryUserServerTree(@RequestBody @Valid UserServerTreeParam.UserServerTreeQuery userServerTreeQuery) {
        return new HttpResult<>(userFacade.queryUserServerTree(userServerTreeQuery));
    }

    @ApiOperation(value = "用户查询配置")
    @GetMapping(value = "/setting/group/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, String>> queryUserSettingByGroup(@RequestParam @Valid String settingGroup) {
        return new HttpResult<>(userSettingFacade.queryUserSettingBySettingGroup(settingGroup));
    }

    @ApiOperation(value = "用户更新配置")
    @PutMapping(value = "/setting/group/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveUserSettingByGroup(@RequestBody @Valid UserSettingParam.UserSetting userSetting) {
        return new HttpResult<>(userSettingFacade.saveUserSettingBySettingGroup(userSetting));
    }

    @ApiOperation(value = "待离职用户查询")
    @GetMapping(value = "/tobe/retired/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<UserVO.User>> queryUserToBeRetired() {
        return new HttpResult<>(userFacade.queryUserToBeRetired());
    }
}
