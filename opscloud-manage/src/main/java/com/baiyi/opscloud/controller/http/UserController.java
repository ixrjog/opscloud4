package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.datasource.facade.UserAmFacade;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.param.user.*;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.user.*;
import com.baiyi.opscloud.facade.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:33 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    private final UserProfileFacade userProfileFacade;

    private final UserGroupFacade userGroupFacade;

    private final UserCredentialFacade userCredentialFacade;

    private final UserUIFacade uiFacade;

    private final UserPermissionFacade permissionFacade;

    private final UserAmFacade userAmFacade;

    @Operation(summary = "查询用户前端界面信息(菜单&UI鉴权)")
    @GetMapping(value = "/ui/info/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UIVO.UIInfo> getUserUIInfo() {
        return new HttpResult<>(uiFacade.buildUIInfo());
    }

    @Operation(summary = "查询用户详情")
    @GetMapping(value = "/details/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.User> getUserDetails() {
        return new HttpResult<>(userFacade.getUserDetails());
    }

    @Operation(summary = "查询用户详情")
    @GetMapping(value = "/details/username/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.User> getUserDetailsByUsername(@RequestParam @Valid String username) {
        return new HttpResult<>(userFacade.getUserDetailsByUsername(username));
    }

    @Operation(summary = "保存用户凭据")
    @PostMapping(value = "/credential/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveUserCredential(@RequestBody @Valid UserCredentialParam.Credential credential) {
        userCredentialFacade.saveCredential(credential);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "分页查询用户列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.User>> queryUserPage(@RequestBody @Valid UserParam.UserPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserPage(pageQuery));
    }

    @Operation(summary = "新增用户")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.User> addUser(@RequestBody @Valid UserParam.CreateUser createUser) {
        return new HttpResult<>(userFacade.addUser(createUser));
    }

    @Operation(summary = "更新用户")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUser(@RequestBody @Valid UserParam.UpdateUser updateUser) {
        userFacade.updateUser(updateUser);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "设置用户是否有效")
    @PutMapping(value = "/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setUserActive(@RequestParam @Valid String username) {
        userFacade.setUserActive(username);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除用户")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteUser(@RequestParam @Valid int id) {
        userFacade.deleteUser(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "同步用户")
    @PutMapping(value = "/sync", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUserPermissionGroupForAsset() {
        userFacade.syncUserPermissionGroupForAsset();
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "新增用户组")
    @PostMapping(value = "/group/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addUserGroup(@RequestBody @Valid UserGroupParam.UserGroup userGroup) {
        userGroupFacade.addUserGroup(userGroup);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新用户组")
    @PutMapping(value = "/group/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUserGroup(@RequestBody @Valid UserGroupParam.UserGroup userGroup) {
        userGroupFacade.updateUserGroup(userGroup);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除用户组")
    @DeleteMapping(value = "/group/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteUserGroup(@RequestParam @Valid Integer id) {
        userGroupFacade.deleteUserGroupById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "解除用户业务许可")
    @PutMapping(value = "/business/permission/revoke", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserBusinessPermission(@RequestBody @Valid UserBusinessPermissionParam.UserBusinessPermission userBusinessPermission) {
        permissionFacade.revokeUserBusinessPermission(userBusinessPermission);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "授予用户业务许可")
    @PostMapping(value = "/business/permission/grant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserBusinessPermission(@RequestBody @Valid UserBusinessPermissionParam.UserBusinessPermission userBusinessPermission) {
        permissionFacade.grantUserBusinessPermission(userBusinessPermission);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "设置用户业务许可（角色）")
    @PutMapping(value = "/business/permission/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setUserBusinessPermission(@RequestParam @Valid int id) {
        permissionFacade.setUserBusinessPermission(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询用户的服务器树")
    @PostMapping(value = "/server/tree/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ServerTreeVO.ServerTree> queryUserServerTree(@RequestBody @Valid ServerGroupParam.UserServerTreeQuery queryParam) {
        return new HttpResult<>(userFacade.queryUserServerTree(queryParam));
    }

    @Operation(summary = "查询用户授权的远程服务器")
    @PostMapping(value = "/server/remote/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerVO.Server>> queryUserRemoteServerPage(@RequestBody @Valid ServerParam.UserRemoteServerPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserRemoteServerPage(pageQuery));
    }

    @Operation(summary = "分页查询用户组列表")
    @PostMapping(value = "/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserGroupVO.UserGroup>> queryUserPage(@RequestBody @Valid UserGroupParam.UserGroupPageQuery pageQuery) {
        return new HttpResult<>(userGroupFacade.queryUserGroupPage(pageQuery));
    }

    @Operation(summary = "分页查询用户授权业务对象列表")
    @PostMapping(value = "/business/permission/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.IUserPermission>> queryUserBusinessPermissionPage(@RequestBody @Valid UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserBusinessPermissionPage(pageQuery));
    }

    @Operation(summary = "分页查询业务对象授权的用户列表")
    @PostMapping(value = "/permission/business/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserVO.User>> queryBusinessPermissionUserPage(@RequestBody @Valid UserBusinessPermissionParam.BusinessPermissionUserPageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryBusinessPermissionUserPage(pageQuery));
    }

    @Operation(summary = "授权用户AccessToken")
    @PostMapping(value = "/access/token/grant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AccessTokenVO.AccessToken> grantUserAccessToken(@RequestBody @Valid AccessTokenParam.ApplicationAccessToken applicationAccessToken) {
        return new HttpResult<>(userFacade.grantUserAccessToken(applicationAccessToken));
    }

    @Operation(summary = "撤销用户AccessToken")
    @PutMapping(value = "/access/token/revoke", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserAccessToken(@RequestParam @Valid String tokenId) {
        userFacade.revokeUserAccessToken(tokenId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询用户AM授权信息")
    @GetMapping(value = "/am/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<AccessManagementVO.XAccessManagement>> queryAmsByUser(@RequestParam @Valid String username, @Valid String amType) {
        return new HttpResult<>(userFacade.queryAmsUser(username, amType));
    }

    @Operation(summary = "创建AM用户")
    @PostMapping(value = "/am/user/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createAmUser(@RequestBody @Valid UserAmParam.CreateUser createUser) {
        userAmFacade.createUser(createUser);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "授权AM用户策略")
    @PostMapping(value = "/am/policy/grant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantAmPolicy(@RequestBody @Valid UserAmParam.GrantPolicy grantPolicy) {
        userAmFacade.grantPolicy(grantPolicy);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "撤销AM用户策略")
    @PutMapping(value = "/am/policy/revoke", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeAmPolicy(@RequestBody @Valid UserAmParam.RevokePolicy revokePolicy) {
        userAmFacade.revokePolicy(revokePolicy);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "用户查询MFA详情")
    @GetMapping(value = "/mfa/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.UserMFA> getUserMFA() {
        return new HttpResult<>(userFacade.getUserMFA());
    }

    @Operation(summary = "用户重置MFA")
    @GetMapping(value = "/mfa/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.UserMFA> resetUserMFA() {
        return new HttpResult<>(userFacade.resetUserMFA());
    }

    @Operation(summary = "用户绑定MFA")
    @GetMapping(value = "/mfa/bind", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.UserMFA> bindUserMFA(@RequestParam @Valid String otp) {
        return new HttpResult<>(userFacade.bindUserMFA(otp));
    }

    @Operation(summary = "用户查询IAM-MFA详情")
    @GetMapping(value = "/mfa/iam/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserVO.UserIAMMFA> getUserIAMMFA() {
        return new HttpResult<>(userFacade.getUserIAMMFA());
    }

    @Operation(summary = "获取用户配置")
    @GetMapping(value = "/profile/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserProfileVO.Profiles> getUserProfiles() {
        return new HttpResult<>(userProfileFacade.getProfiles());
    }

}