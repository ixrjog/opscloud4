package com.sdg.cmdb.controller;

import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.keybox.ApplicationKeyVO;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerVO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 2016/11/20.
 */
@Controller
@RequestMapping("/box")
public class KeyBoxController {

    @Resource
    private KeyBoxService keyBoxService;

    @Resource
    private ConfigService configService;

    @Resource
    private AuthService authService;

    @Resource
    private LdapService ldapService;

//    @Resource
//    private ZabbixService zabbixService;

    @Resource
    private UserDao userDao;

    @Resource
    private SchedulerManager schedulerManager;

    /**
     * 堡垒机分页数据查询
     *
     * @param userServerDO
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getBoxPage(
            @RequestBody KeyboxUserServerDO userServerDO,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(keyBoxService.getUserServerPage(userServerDO, page, length));
    }

    /**
     * 堡垒机授权
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/auth/add", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult doAuthKeybox(@RequestParam String username) {
        return new HttpResult(keyBoxService.authUserKeybox(username));

    }

    /**
     * 堡垒机删除授权
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/auth/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delAuthKeybox(@RequestParam String username) {
        return new HttpResult(keyBoxService.delUserKeybox(username));
    }

    /**
     * 获取堡垒机用户服务器组分页信息
     *
     * @param userServerDO
     * @return
     */
    @RequestMapping(value = "/user/group", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getUserGroupPage(@RequestBody KeyboxUserServerDO userServerDO,
                                       @RequestParam int page, @RequestParam int length) {

        return new HttpResult(keyBoxService.getUserServerPage(userServerDO, page, length));
    }


    /**
     * 添加新的堡垒机用户服务器组
     *
     * @param userServerVO
     * @return
     */
    @RequestMapping(value = "/user/group/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveUserGroup(@RequestBody KeyboxUserServerVO userServerVO) {
        return new HttpResult(keyBoxService.saveUserGroup(userServerVO));
    }


    /**
     * 删除用户服务器组
     *
     * @param groupId
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/group/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delUserGroup(@RequestParam long groupId, @RequestParam String username) {
        KeyboxUserServerDO userServerDO = new KeyboxUserServerDO();
        userServerDO.setServerGroupId(groupId);
        userServerDO.setUsername(username);
        BusinessWrapper<Boolean> wrapper = keyBoxService.delUserGroup(userServerDO);
        return new HttpResult(wrapper);
    }


    /**
     * 查询被授权的堡垒机服务器列表
     * 修改管理员默认可以查看所有服务器（web）
     *
     * @param serverGroupId
     * @param envType
     * @return
     */
    @RequestMapping(value = "/server/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getBoxServerPage(
            @RequestParam long serverGroupId, @RequestParam int envType,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(keyBoxService.getServerList(serverGroupId, envType, page, length));
    }


    /**
     * 校验用户（删除离职用户的关联数据）
     *
     * @return
     */
    @RequestMapping(value = "/checkUser", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult checkUser() {
        return new HttpResult(keyBoxService.checkUser());
    }


    /**
     * 堡垒机服务器组用户管理分页数据查询
     *
     * @param serverGroupId
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/group/user/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getBoxGroupUserPage(
            @RequestParam long serverGroupId,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(keyBoxService.getBoxGroupUserPage(serverGroupId, page, length));
    }


    @RequestMapping(value = "/key/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getKeyManage() {
        return new HttpResult(keyBoxService.getApplicationKey());
    }


    @RequestMapping(value = "/key/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getBoxKeySave(
            @RequestBody ApplicationKeyVO applicationKeyVO) {
        return new HttpResult(keyBoxService.saveApplicationKey(applicationKeyVO));
    }


    /**
     * 保存用户
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveUser(@RequestBody UserVO userVO) {
        BusinessWrapper<Boolean> wrapper = authService.addUser(userVO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 用户自己修改个人详情
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "/user/saveDetail", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult updateUserDetail(@RequestBody UserVO userVO) {
        BusinessWrapper<Boolean> wrapper = ldapService.updateUser(userVO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }


    /**
     * 堡垒机使用统计
     *
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult statusKeybox() {
        return new HttpResult(keyBoxService.keyboxStatus());
    }


    /**
     * 堡垒机使用统计
     *
     * @return
     */
    @RequestMapping(value = "/getway/status", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult statusGetway(@RequestParam String username, @RequestParam String ip) {
        return new HttpResult(keyBoxService.getwayStatus(username, ip));
    }


}
