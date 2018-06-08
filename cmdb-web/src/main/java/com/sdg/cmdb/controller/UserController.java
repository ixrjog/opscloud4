package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.CiUserGroupVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserLeaveVO;
import com.sdg.cmdb.domain.scm.ScmPermissionsVO;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.CiUserGroupService;
import com.sdg.cmdb.service.GitService;
import com.sdg.cmdb.service.UserService;
import com.sdg.cmdb.zabbix.ZabbixService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 2016/11/22.
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    @Resource
    private GitService gitService;

    @Resource
    private CiUserGroupService ciUserGroupService;

    @Resource
    private ZabbixService zabbixService;

    /**
     * 获取指定条件的用户集合
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getUserPage(@RequestParam String username, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(userService.getUserPage(username, page, length));
    }

    /**
     * 获取指定条件的(不含有私密信息)用户集合
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/safe/users", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getSafeUserPage(@RequestParam String username, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(userService.getSafeUserPage(username, page, length));
    }

    /**
     * 获取指定条件的离职用户集合
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/usersLeave/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getUserLeavePage(@RequestParam String username, @RequestParam int page, @RequestParam int length) {

        TableVO<List<UserLeaveVO>> tableVO = userService.getUserLeavePage(username, page, length);
        for (UserLeaveVO userLeaveVO : tableVO.getData()) {
            userLeaveVO.setZabbix(zabbixService.userGet(new UserDO(userLeaveVO.getUsername())));
        }

        return new HttpResult(userService.getUserLeavePage(username, page, length));
    }

    /**
     * 删除离职用户数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/usersLeave/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delUserLeave(@RequestParam long id) {
        return new HttpResult(userService.delUserLeave(id));
    }


    /**
     * 获取指定条件的用户集合
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/cmdb/users", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCmdbUserPage(@RequestParam String username, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(userService.getCmdbUserPage(username, page, length));
    }

    /**
     * 获取指定条件的用户集合(开放外部接口)
     *
     * @param authKey
     * @param username
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCmdbUserPage(@RequestParam String authKey, @RequestParam String username, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(userService.getCmdbApiUserPage(authKey, username, page, length));
    }

    /**
     * 获取单个用户详细信息（权限）
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/user", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCmdbUser(@RequestParam String username) {
        return new HttpResult(userService.getCmdbUser(username));
    }


    /**
     * 所有的ldap组中移除该用户
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/ldapGroup/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delMember2Group(@RequestParam String username) {
        return new HttpResult(authService.removeMember2Group(username));
    }


    /**
     * 所有的ldap组中移除该用户
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/ldap/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delLdapUser(@RequestParam String username) {
        return new HttpResult(authService.unbindUser(username));
    }


    /**
     * 关闭用户邮箱
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/zabbix/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delZabbixUser(@RequestParam String username) {
        return new HttpResult(zabbixService.userDelete(new UserDO(username)));
    }


    /**
     * 关闭用户邮箱
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/mailLdap/close", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult closeUserMail(@RequestParam String username) {
        return new HttpResult(authService.closeMailAccount(username));
    }

    /**
     * 激活用户邮箱
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/mailLdap/active", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult activeUserMail(@RequestParam String username) {
        return new HttpResult(authService.activeMailAccount(username));
    }

    /**
     * 添加用户权限
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/ldapGroup/add", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addLdapGroup(@RequestParam String username, @RequestParam String groupname) {
        return new HttpResult(authService.addLdapGroup(username, groupname));
    }

    /**
     * 删除用户权限
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/ldapGroup/del", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult delLdapGroup(@RequestParam String username, @RequestParam String groupname) {
        return new HttpResult(authService.delLdapGroup(username, groupname));
    }


    /**
     * 离职接口
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/user/leave", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delUserLeave(@RequestParam String username) {
        return new HttpResult(authService.delUser(username));
    }

    /**
     * 获取指定条件持续集成用户组集合
     *
     * @param groupName
     * @param envType
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/ci/usergroup/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCiUserGroupPage(@RequestParam String groupName, @RequestParam int envType, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ciUserGroupService.getCiUserGroupPage(groupName, envType, page, length));
    }

    /**
     * 更新数据
     *
     * @return
     */
    @RequestMapping(value = "/ci/usergroup/refresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCiUsergroupRefresh() {
        return new HttpResult(ciUserGroupService.groupsRefresh());
    }

    /**
     * 删除持续集成用户组
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/ci/usergroup/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delCigroup(@RequestParam long id) {
        return new HttpResult(ciUserGroupService.delCigroup(id));
    }


    /**
     * 获取指定条件的持续集成集合
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/cmdb/ci/users", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCmdbCiUserPage(@RequestParam String username, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ciUserGroupService.getCiUserPage(username, page, length));
    }

    /**
     * 用户持续集成权限信息
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/ci/user", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCmdbCiUser(@RequestParam String username) {
        return new HttpResult(ciUserGroupService.getCiUser(username));
    }


    /**
     * 添加用户的持续集成权限组
     *
     * @param userId
     * @param usergroupId
     * @return
     */
    @RequestMapping(value = "/cmdb/ci/users/addGroup", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCmdbCiUserAddGroup(@RequestParam long userId, @RequestParam long usergroupId) {
        return new HttpResult(ciUserGroupService.userAddGroup(userId, usergroupId));
    }


    @RequestMapping(value = "/cmdb/ci/users/delGroup", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delCmdbCiUser(@RequestParam long ciuserId) {
        return new HttpResult(ciUserGroupService.userDelGroup(ciuserId));
    }


    /**
     * 保存指定的ciUserGroupItem
     *
     * @param ciUserGroupVO
     * @return
     */
    @RequestMapping(value = "/ci/usergroup/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveCiUserGroup(@RequestBody CiUserGroupVO ciUserGroupVO) {
        BusinessWrapper<Boolean> wrapper = ciUserGroupService.saveCiUserGroup(ciUserGroupVO);
        // 变更配置文件
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }


    /**
     * 更新数据
     *
     * @return
     */
    @RequestMapping(value = "/cmdb/ci/users/refresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCiUsersRefresh() {
        return new HttpResult(ciUserGroupService.usersRefresh());
    }


    /**
     * 批量填充用户手机号
     *
     * @return
     */
    @RequestMapping(value = "/cmdb/users/addUsersMobile", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addUsersMobile() {
        return new HttpResult(userService.addUsersMobile());
    }

    /**
     * 填充用户手机号
     *
     * @return
     */
    @RequestMapping(value = "/cmdb/user/addUsersMobile", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addUsersMobile(@RequestParam long userId) {
        return new HttpResult(userService.addUserMobile(userId));
    }


    /**
     * 获取指定条件的SCM权限组集合
     *
     * @param groupName
     * @param scmProject
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/scm/permissions/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getScmPermissionsPage(@RequestParam String groupName, @RequestParam String scmProject, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(gitService.queryScmPermissionsPage(groupName, scmProject, page, length));
    }

    @RequestMapping(value = "/scm/permissions/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getScmPermissionsGet(@RequestParam String scmProject) {
        return new HttpResult(gitService.getScmPermissions(scmProject));
    }

    /**
     * 刷新SCM权限组集合
     *
     * @return
     */
    @RequestMapping(value = "/scm/permissions/refresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getScmPermissionsPage() {
        return new HttpResult(gitService.scmPermissionsRefresh());
    }


    /**
     * 刷新SCM权限组集合
     *
     * @return
     */
    @RequestMapping(value = "/scm/permissions/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getScmPermissionsSave(@RequestBody ScmPermissionsVO scmPermissionsVO) {
        return new HttpResult(gitService.savePermissions(scmPermissionsVO));
    }
}
