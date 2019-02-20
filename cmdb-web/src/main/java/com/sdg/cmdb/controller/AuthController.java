package com.sdg.cmdb.controller;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.auth.*;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 16/9/18.
 */
@Controller
public class AuthController {

    private static final String token = "token";

    @Resource
    private AuthService authService;

    @Resource
    private UserService userService;

    @RequestMapping("/dashboard")
    public String index() {
        return "index";
    }

    /**
     * 登录凭证校验
     *
     * @param userName
     * @param pwd
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult login(String userName, String pwd) {
        BusinessWrapper<UserDO> wrapper = authService.loginCredentialCheck(userName, pwd);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 外部认证接口
     *
     * @param userDO
     * @return
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult login(@RequestBody UserDO userDO) {
        return authService.apiLoginCheck(userDO);
    }


    /**
     * 登出
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult logout(@RequestParam String username) {
        BusinessWrapper<Boolean> wrapper = authService.logout(username);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 权限校验
     *
     * @param headers
     * @param checkUrl
     * @param authGroup
     * @return
     */
    @RequestMapping(value = "/check/auth", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult checkAuth(@RequestHeader HttpHeaders headers, @RequestParam String checkUrl, @RequestParam(defaultValue = "[]", required = false) String authGroup) {
        if (!headers.containsKey(token)) {
            return new HttpResult(ErrorCode.requestNoToken.getCode(), ErrorCode.requestNoToken.getMsg());
        }
        if (authGroup.equals("undefined")) {
            authGroup = "[]";
        }
        String tokenValue = headers.getFirst(token);
        BusinessWrapper<List<ResourceDO>> wrapper = authService.checkAndGetUserHasResourceAuthorize(tokenValue, checkUrl, JSON.parseArray(authGroup, String.class));

        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 获取满足要求的资源组数据
     *
     * @param groupCode
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/resource/groups", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getResourceGroup(@RequestParam String groupCode, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(authService.getResourceGroups(groupCode, page, length));
    }

    /**
     * 保存指定的资源组数据
     *
     * @param resourceGroupDO
     * @return
     */
    @RequestMapping(value = "/resource/group/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveResourceGroup(@RequestBody ResourceGroupDO resourceGroupDO) {
        BusinessWrapper<Boolean> wrapper = authService.saveResourceGroup(resourceGroupDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定的资源组信息
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/resource/group/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delResourceGroup(@RequestParam long groupId) {
        BusinessWrapper<Boolean> wrapper = authService.delResourceGroup(groupId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 查询合适条件的资源数据集合
     *
     * @param groupId
     * @param resourceName
     * @param authType
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/resource/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getResources(
            @RequestParam long groupId, @RequestParam String resourceName, @RequestParam int authType,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(authService.getResources(groupId, resourceName, authType, page, length));
    }

    /**
     * 更新或添加资源
     *
     * @param resourceVO
     * @return
     */
    @RequestMapping(value = "/resource/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveResource(@RequestBody ResourceVO resourceVO) {
        BusinessWrapper<Boolean> wrapper = authService.saveResource(resourceVO);

        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定的资源
     *
     * @param resourceId
     * @return
     */
    @RequestMapping(value = "/resource/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delResource(@RequestParam long resourceId) {
        BusinessWrapper<Boolean> wrapper = authService.delResource(resourceId);

        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 获取角色集合
     *
     * @param resourceId
     * @param roleName
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/role/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getRoles(
            @RequestParam long resourceId, @RequestParam String roleName,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(authService.getRoles(resourceId, roleName, page, length));
    }

    /**
     * 保存角色信息
     *
     * @param roleDO
     * @return
     */
    @RequestMapping(value = "/role/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveRole(@RequestBody RoleDO roleDO) {
        BusinessWrapper<Boolean> wrapper = authService.saveRole(roleDO);

        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除指定的角色信息
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/role/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delRole(@RequestParam long roleId) {
        BusinessWrapper<Boolean> wrapper = authService.delRole(roleId);

        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 获取指定角色id的绑定资源列表
     *
     * @param roleId
     * @param resourceGroupId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/role/resource/bind/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getRoleResourceBind(
            @RequestParam long roleId, @RequestParam long resourceGroupId,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(authService.getRoleBindResources(roleId, resourceGroupId, page, length));
    }

    /**
     * 获取指定角色id的未绑定资源列表
     *
     * @param roleId
     * @param resourceGroupId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/role/resource/unbind/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getRoleResourceUnbind(
            @RequestParam long roleId, @RequestParam long resourceGroupId,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(authService.getRoleUnbindResources(roleId, resourceGroupId, page, length));
    }

    /**
     * 角色资源建立绑定关系
     *
     * @param roleId
     * @param resourceId
     * @return
     */
    @RequestMapping(value = "/role/resource/bind", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult roleResourceBind(long roleId, long resourceId) {
        BusinessWrapper<Boolean> wrapper = authService.roleResourceBind(roleId, resourceId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 角色资源解除绑定关系
     *
     * @param roleId
     * @param resourceId
     * @return
     */
    @RequestMapping(value = "/role/resource/unbind", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult roleResourceUnbind(long roleId, long resourceId) {
        BusinessWrapper<Boolean> wrapper = authService.roleResourceUnbind(roleId, resourceId);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 获取用户角色数据
     *
     * @param roleId
     * @param username
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/user/role", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult userRoles(@RequestParam long roleId, @RequestParam String username,
                                @RequestParam int page, @RequestParam int length) {
        return new HttpResult(authService.getUsers(username, roleId, page, length));
    }

    /**
     * 用户角色绑定
     *
     * @param roleId
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/role/bind", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult userRoleBind(long roleId, String username) {
        BusinessWrapper<Boolean> wrapper = authService.userRoleBind(roleId, username);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 用户角色解绑
     *
     * @param roleId
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/role/unbind", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult userRoleUnbind(long roleId, String username) {
        BusinessWrapper<Boolean> wrapper = authService.userRoleUnbind(roleId, username);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 查询指定用户名的用户信息
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryUserInfo(@RequestParam String username) {
        BusinessWrapper<UserVO> wrapper = userService.getUserVOByName(username);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 更新用户信息
     *
     * @param userDO
     * @return
     */
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveUserInfo(@RequestBody UserDO userDO) {
        BusinessWrapper<Boolean> wrapper = userService.saveUserInfo(userDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 重置token
     *
     * @return
     */
    @RequestMapping(value = "/user/resetToken", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult userRoles() {
        return new HttpResult(authService.resetToken());
    }

}
