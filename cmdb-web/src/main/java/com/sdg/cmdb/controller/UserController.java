package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.CiUserGroupVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserLeaveVO;
import com.sdg.cmdb.domain.scm.ScmPermissionsVO;
import com.sdg.cmdb.domain.zabbix.response.ZabbixResponseUser;
import com.sdg.cmdb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户管理
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;


    @Resource
    private LdapService ldapService;


    @Resource
    private CiUserGroupService ciUserGroupService;

    @Resource
    private ZabbixService zabbixService;

    @Resource
    private ZabbixServerService zabbixServerService;

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
            ZabbixResponseUser zabbixUser = zabbixServerService.getUser(new UserDO(userLeaveVO.getUsername()));
            if (zabbixUser != null && !StringUtils.isEmpty(zabbixUser.getUserid()))
                userLeaveVO.setZabbix(1);
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
        return new HttpResult(ldapService.removeMember(username));
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
        return new HttpResult(ldapService.unbindUser(username));
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
     * 添加用户权限
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/cmdb/ldapGroup/add", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addLdapGroup(@RequestParam String username, @RequestParam String groupname) {
        return new HttpResult(ldapService.addLdapGroup(username, groupname));
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
        return new HttpResult(ldapService.delLdapGroup(username, groupname));
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



    @RequestMapping(value = "/app/user/exclude", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addUsersMobile(@RequestParam String username,@RequestParam long appId) {
        return new HttpResult(userService.queryUserExcludeApp(username,appId));
    }

    @RequestMapping(value = "/app/user/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryUserByApp(@RequestParam long appId) {
        return new HttpResult(userService.queryUserByApp(appId));
    }

    @RequestMapping(value = "/app/user/add", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addUserByApp(@RequestParam long appId,@RequestParam long userId) {
        return new HttpResult(userService.addUserByApp(appId,userId));
    }

    @RequestMapping(value = "/app/user/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delUserByApp(@RequestParam long appId,@RequestParam long userId) {
        return new HttpResult(userService.delUserByApp(appId,userId));
    }


}
