package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.task.CmdVO;
import com.sdg.cmdb.service.AnsibleTaskService;
import com.sdg.cmdb.service.ServerTaskService;
import com.sdg.cmdb.zabbix.LogCleanupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/3/31.
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    @Resource
    private LogCleanupService logCleanupService;

    @Resource
    private ServerTaskService serverTaskService;

    @Resource
    private AnsibleTaskService ansibleTaskService;

    /**
     * 日志弹性清理分页数据查询
     *
     * @param serverGroupId
     * @param serverName
     * @param enabled
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/logcleanup/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLogCleanupPage(
            @RequestParam long serverGroupId,
            @RequestParam String serverName,
            @RequestParam int enabled,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(logCleanupService.getLogCleanupPage(serverGroupId, serverName, enabled, page, length));
    }

    /**
     * 清理服务器日志
     *
     * @param serverId
     * @return
     */
    @RequestMapping(value = "/logcleanup/cleanup", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCleanup(
            @RequestParam long serverId) {
        return new HttpResult(logCleanupService.cleanup(serverId));
    }

    /**
     * 同步数据
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/refresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getRefresh() {
        logCleanupService.syncData();
        return new HttpResult(new BusinessWrapper<>(true));
    }

    /**
     * 同步数据
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/setEnabled", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getSetEnabled(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(logCleanupService.setEnabled(serverId)));
    }

    /**
     * 减少清理日期
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/subtractHistory", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getSubtractHistory(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(logCleanupService.setHistory(serverId, -1)));
    }

    /**
     * 增加清理日期
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/addHistory", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getAddHistory(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(logCleanupService.setHistory(serverId, 1)));
    }

    /**
     * 刷新磁盘使用率
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/refreshDiskRate", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getRefreshDiskRate(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(logCleanupService.refreshDiskRate(serverId)));
    }


    /**
     * 初始化系统
     *
     * @return
     */
    @RequestMapping(value = "/servertask/initializationSystem", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getInitializationSystem(@RequestParam long serverId) {
        return new HttpResult(new BusinessWrapper<>(serverTaskService.initializationSystem(serverId)));
    }


    /**
     * 修改日志保留天数
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/save", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getSaveLogcleanup(@RequestParam long id, @RequestParam int history) {

        return new HttpResult(new BusinessWrapper<>(logCleanupService.saveHistory(id, history)));
    }


    @RequestMapping(value = "/cmd/doCmd", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult postDoCmd(@RequestBody CmdVO cmdVO) {
        return new HttpResult(ansibleTaskService.cmdTask(cmdVO));
    }

    @RequestMapping(value = "/cmd/doScript", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult postDoScript(@RequestBody CmdVO cmdVO) {
        return new HttpResult(ansibleTaskService.scriptTask(cmdVO));
    }

    @RequestMapping(value = "/cmd/copySever/doScript", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult postDoScript(@RequestParam long id) {
        return new HttpResult(ansibleTaskService.doScriptByCopyServer(id));
    }

    @RequestMapping(value = "/cmd/copySever/doScriptByGroup", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult postDoScript(@RequestParam String groupName) {
        return new HttpResult(ansibleTaskService.doScriptByCopyByGroup(groupName));
    }

    @RequestMapping(value = "/cmd/doScript/updateGetway", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult doUpdateGetway() {
        return new HttpResult(ansibleTaskService.scriptTaskUpdateGetway());
    }


    @RequestMapping(value = "/cmd/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult postDoCmd(@RequestParam long taskId) {
        return new HttpResult(ansibleTaskService.taskQuery(taskId));
    }


    @RequestMapping(value = "/copy/doFileCopy", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult doFileCopy(@RequestParam long id) {
        return new HttpResult(ansibleTaskService.doFileCopy(id));
    }


    @RequestMapping(value = "/copy/doFileCopyAll", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult doFileCopyAll(@RequestParam String groupName) {
        return new HttpResult(ansibleTaskService.doFileCopyByFileGroupName(groupName));
    }




    /**
     * ansible 脚本详情页
     *
     * @param scriptName
     * @param sysScript
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/script/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getTaskScriptPage(
            @RequestParam String scriptName,
            @RequestParam int sysScript,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ansibleTaskService.getTaskScriptPage(scriptName, sysScript, page, length));
    }

    @RequestMapping(value = "/script/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult postDoCmd(@RequestBody TaskScriptDO taskScriptDO) {

        return new HttpResult(ansibleTaskService.saveTaskScript(taskScriptDO));
    }

    @RequestMapping(value = "/ansible/version", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getAnsibleVersion() {
        return new HttpResult(ansibleTaskService.acqAnsibleVersion());
    }


    @RequestMapping(value = "/ansibleTask/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getAnsibleTaskPage(
            @RequestParam String cmd,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ansibleTaskService.getAnsibleTaskPage(cmd, page, length));
    }

}
