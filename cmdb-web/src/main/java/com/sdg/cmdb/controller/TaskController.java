package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ansibleTask.TaskScriptDO;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.logCleanup.LogcleanupDO;
import com.sdg.cmdb.domain.task.CmdVO;
import com.sdg.cmdb.domain.task.DoPlaybook;
import com.sdg.cmdb.service.AnsibleTaskService;
import com.sdg.cmdb.service.LogcleanupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liangjian on 2017/3/31.
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private LogcleanupService logcleanupService;

    @Autowired
    private AnsibleTaskService ansibleTaskService;

    /**
     * 日志弹性清理分页数据查询
     *
     * @param serverGroupId
     * @param serverName
     * @param enabled
     * @param page
     * @param length
     * @return //
     */
    @RequestMapping(value = "/logcleanup/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLogcleanupPage(
            @RequestParam long serverGroupId,
            @RequestParam String serverName,
            @RequestParam int enabled,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(logcleanupService.getLogcleanupPage(serverGroupId, serverName, enabled, page, length));
    }

    /**
     * 查询清理任务日志
     *
     * @param id
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/logcleanup/log/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLogcleanupTaskPage(
            @RequestParam long id,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(logcleanupService.getLogcleanupTaskLogPage(id, page, length));
    }

    /**
     * 修改配置
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getSaveLogcleanup(@RequestBody LogcleanupDO logcleanupDO) {
        return new HttpResult(logcleanupService.save(logcleanupDO));
    }

    /**
     * 更新数据（采集）
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/update", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult updateLogcleanup(@RequestParam long serverId) {
        return new HttpResult(logcleanupService.updateLogcleanupConfig(serverId));
    }

    /**
     * 刷新
     *
     * @return
     */
    @RequestMapping(value = "/logcleanup/refresh", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult updateServers() {
        return new HttpResult(logcleanupService.updateLogcleanupServers());
    }

    @RequestMapping(value = "/logcleanup/doTask", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult doLogcleanupTask(@RequestParam long id) {
        return new HttpResult(logcleanupService.doTask(id));
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

    @RequestMapping(value = "/playbook/do", method = RequestMethod.POST)
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
