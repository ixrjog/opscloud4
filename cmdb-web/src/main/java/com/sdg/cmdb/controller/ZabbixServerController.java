package com.sdg.cmdb.controller;

import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.zabbix.ZabbixHost;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.zabbix.ZabbixService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/1/13.
 */
@Controller
@RequestMapping("/zabbixserver")
public class ZabbixServerController {

    @Resource
    private ServerService serverService;

    @Resource
    private ZabbixService zabbixService;

    @Resource
    private UserDao userDao;


    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVersion(@RequestParam String zabbixServerName) {
        return new HttpResult(zabbixService.getZabbixVersion(zabbixServerName));
    }


    @RequestMapping(value = "/host/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVersion(@RequestParam long serverId) {
        return new HttpResult(zabbixService.getHost(serverId));
    }

    @RequestMapping(value = "/host/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getVersion(@RequestBody ZabbixHost zabbixHost) {
        return new HttpResult(zabbixService.saveHost(zabbixHost));
    }

    @RequestMapping(value = "/template/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryTemplatePage(@RequestParam String templateName, @RequestParam int enabled,
                                        @RequestParam int page, @RequestParam int length) {
        return new HttpResult(zabbixService.getTemplatePage(templateName, enabled, page, length));
    }


//    @RequestMapping(value = "/template/get", method = RequestMethod.GET)
//    @ResponseBody
//    public HttpResult queryTemplateGet(@RequestParam long serverGroupId) {
//        return new HttpResult(zabbixService.getTemplates(serverGroupId));
//    }


    @RequestMapping(value = "/template/set", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult setTemplate(@RequestParam long id) {
        return new HttpResult(zabbixService.setTemplate(id));
    }

    @RequestMapping(value = "/template/rsync", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult rsyncTemplate() {
        return new HttpResult(zabbixService.rsyncTemplate());
    }

    @RequestMapping(value = "/template/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delTemplate(@RequestParam long id) {
        return new HttpResult(zabbixService.delTemplate(id));
    }

    @RequestMapping(value = "/proxy/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryProxy() {
        return new HttpResult(zabbixService.queryProxy());
    }

//    @RequestMapping(value = "/proxy/get", method = RequestMethod.GET)
//    @ResponseBody
//    public HttpResult queryProxy(@RequestParam long serverGroupId) {
//        return new HttpResult(zabbixService.getProxy(serverGroupId));
//    }

    /**
     * 获取指定条件的服务器列表分页数据
     *
     * @param serverGroupId
     * @param serverName
     * @param useType
     * @param envType
     * @param queryIp
     * @param zabbixStatus
     * @param zabbixMonitor
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryServerPage(@RequestParam long serverGroupId, @RequestParam String serverName,
                                      @RequestParam int useType, @RequestParam int envType, @RequestParam String queryIp,
                                      @RequestParam int zabbixStatus, @RequestParam int zabbixMonitor, @RequestParam String tomcatVersion,
                                      @RequestParam int page, @RequestParam int length) {
        return new HttpResult(serverService.getZabbixServerPage(serverGroupId, serverName, useType, envType, queryIp, zabbixStatus, zabbixMonitor, tomcatVersion, page, length));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryServerPage() {
        return new HttpResult(zabbixService.refresh());
    }

    @RequestMapping(value = "/addMonitor", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addMonitor(@RequestParam long serverId) {
        return new HttpResult(zabbixService.addMonitor(serverId));
    }

    @RequestMapping(value = "/delMonitor", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult delMonitor(@RequestParam long serverId) {
        return new HttpResult(zabbixService.delMonitor(serverId));
    }


    @RequestMapping(value = "/repair", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult repair(@RequestParam long serverId) {
        return new HttpResult(zabbixService.repair(serverId));
    }


    @RequestMapping(value = "/disableMonitor", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult disableMonitor(@RequestParam long serverId) {
        return new HttpResult(zabbixService.disableMonitor(serverId));
    }

    @RequestMapping(value = "/enableMonitor", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult enableMonitor(@RequestParam long serverId) {
        return new HttpResult(zabbixService.enableMonitor(serverId));
    }

    /**
     * 持续集成接口:用于记录和停止监控
     *
     * @param type
     * @param project
     * @param group
     * @param env
     * @return
     */
    @RequestMapping(value = "/ci", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult ci(@RequestParam String key, @RequestParam int type, @RequestParam String project, @RequestParam String group,
                         @RequestParam String env, @RequestParam Long deployId, @RequestParam String bambooDeployVersion,
                         @RequestParam int bambooBuildNumber, @RequestParam String bambooDeployProject, @RequestParam boolean bambooDeployRollback,
                         @RequestParam String bambooManualBuildTriggerReasonUserName, @RequestParam int errorCode, @RequestParam String branchName, @RequestParam int deployType) {
        /*
         https://cmdb.51xianqu.net/api/zabbix/monitor/type=1&key=xxxxxxx&project=cmdb&group=cmdb-production&env=production
          */
        BusinessWrapper<Boolean> wrapper = zabbixService.ci(key, type, project, group, env, deployId, bambooDeployVersion, bambooBuildNumber, bambooDeployProject, bambooDeployRollback, bambooManualBuildTriggerReasonUserName, errorCode, branchName, deployType);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 授权
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/auth/add", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult doAuthZabbix(@RequestParam String username) {
        UserDO userDO = userDao.getUserByName(username);
        BusinessWrapper<Boolean> wrapper = zabbixService.userCreate(userDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 删除授权
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/auth/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delAuthKeybox(@RequestParam String username) {
        UserDO userDO = userDao.getUserByName(username);
        BusinessWrapper<Boolean> wrapper = zabbixService.userDelete(userDO);
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 同步zabbix账户
     *
     * @return
     */
    @RequestMapping(value = "/user/sync", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult doSyncZabbixUser() {
        BusinessWrapper<Boolean> wrapper = zabbixService.syncUser();
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    /**
     * 同步zabbix账户
     *
     * @return
     */
    @RequestMapping(value = "/user/check", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult doCheckZabbixUser() {
        BusinessWrapper<Boolean> wrapper = zabbixService.checkUser();
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }


}
