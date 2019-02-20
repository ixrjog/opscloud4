package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.task.CmdVO;
import com.sdg.cmdb.domain.task.DoPlaybook;
import com.sdg.cmdb.service.AnsibleTaskService;
import com.sdg.cmdb.service.LogCleanupService;
import com.sdg.cmdb.service.ServerTaskService;
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

    @RequestMapping(value = "/cmd/doPlaybook", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult doPlaybook(@RequestBody DoPlaybook doPlaybook) {
        return new HttpResult(ansibleTaskService.playbookTask(doPlaybook));
    }


    @RequestMapping(value = "/cmd/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult postDoCmd(@RequestParam long taskId) {
        return new HttpResult(ansibleTaskService.taskQuery(taskId));
    }

    /**
     * 查询PlaybookTask
     */
    @RequestMapping(value = "/playbook/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getPlaybookTaskVO(@RequestParam long id) {
        return new HttpResult(ansibleTaskService.getPlaybookTask(id));
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

    @RequestMapping(value = "/script/getPlaybook", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getTaskScriptPlaybook() {
        return new HttpResult(ansibleTaskService.getTaskScriptPlaybook());
    }

    @RequestMapping(value = "/script/queryPlaybook", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryPlaybook(@RequestParam String playbookName) {
        return new HttpResult(ansibleTaskService.queryPlaybook(playbookName));
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

    @RequestMapping(value = "/ansible/playbook/version", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getAnsiblePlaybookVersion() {
        return new HttpResult(ansibleTaskService.acqAnsiblePlaybookVersion());
    }


    @RequestMapping(value = "/ansibleTask/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getAnsibleTaskPage(
            @RequestParam String cmd,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ansibleTaskService.getAnsibleTaskPage(cmd, page, length));
    }

}
