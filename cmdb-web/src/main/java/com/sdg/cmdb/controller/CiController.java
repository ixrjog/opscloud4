package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.ci.CiAppVO;
import com.sdg.cmdb.domain.ci.CiJobParamDO;
import com.sdg.cmdb.domain.ci.CiJobVO;
import com.sdg.cmdb.domain.ci.CiTemplateDO;
import com.sdg.cmdb.domain.ci.jenkins.Notify;
import com.sdg.cmdb.service.CiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("ci")
public class CiController {


    @Autowired
    private CiService ciService;

    @RequestMapping(value = "/v1/buildNotify", method = RequestMethod.POST)
    public HttpResult ciBuildNotify(@RequestBody Notify notify) {
        return new HttpResult(ciService.buildNotify(notify));
    }

    /**
     * 查询我的App
     *
     * @param projectName
     * @return
     */
    @RequestMapping(value = "/app/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getMyApp(@RequestParam String projectName) {
        return new HttpResult(ciService.queryMyApp(projectName));
    }

    /**
     * 查询App
     *
     * @param appId
     * @return
     */
    @RequestMapping(value = "/app/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getMyApp(@RequestParam long appId) {
        return new HttpResult(ciService.getApp(appId));
    }

    /**
     * 保存ciApp
     *
     * @param ciAppVO
     * @return
     */
    @RequestMapping(value = "/app/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveApp(@RequestBody CiAppVO ciAppVO) {
        return new HttpResult(ciService.saveApp(ciAppVO));
    }


    /**
     * 保存ciJob
     *
     * @param ciJobVO
     * @return
     */
    @RequestMapping(value = "/job/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveJob(@RequestBody CiJobVO ciJobVO) {
        return new HttpResult(ciService.saveJob(ciJobVO));
    }

    @RequestMapping(value = "/job/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryJob(@RequestParam long appId) {
        return new HttpResult(ciService.queryJob(appId));
    }

    @RequestMapping(value = "/job/create", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult createJob(@RequestParam long jobId) {
        return new HttpResult(ciService.createJob(jobId));
    }

    @RequestMapping(value = "/job/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getJob(@RequestParam long id) {
        return new HttpResult(ciService.getJob(id));
    }

    @RequestMapping(value = "/job/buildById", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult buildJobById(@RequestParam long id) {
        return new HttpResult(ciService.buildJob(id));
    }

    @RequestMapping(value = "/job/build", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult buildJob(@RequestBody CiJobVO ciJobVO) {
        return new HttpResult(ciService.buildJob(ciJobVO));
    }

    @RequestMapping(value = "/job/deploy", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult deployJob(@RequestBody CiJobVO ciJobVO) {
        return new HttpResult(ciService.deployJob(ciJobVO));
    }

    @RequestMapping(value = "/job/param/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult addParam(@RequestBody CiJobParamDO ciJobParamDO) {
        return new HttpResult(ciService.saveParam(ciJobParamDO));
    }

    @RequestMapping(value = "/job/param/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult addParam(@RequestParam long id) {
        return new HttpResult(ciService.delParam(id));
    }


    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVersion() {
        return new HttpResult(ciService.getJenkinsVersion());
    }

    @RequestMapping(value = "/template/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getTemplatePage(@RequestParam String name, @RequestParam int appType, @RequestParam int ciType, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ciService.getTemplatePage(name, appType, ciType, page, length),true);
    }

    @RequestMapping(value = "/jenkins/template", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getJenkinsTemplate() {
        return new HttpResult(ciService.getJenkinsTemplate());
    }

    @RequestMapping(value = "/template/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveTemplate(@RequestBody CiTemplateDO ciTemplateDO) {
        return new HttpResult(ciService.saveTemplate(ciTemplateDO));
    }

    @RequestMapping(value = "/template/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delTemplate(@RequestParam long id) {
        return new HttpResult(ciService.delTemplate(id));
    }

    @RequestMapping(value = "/template/update", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult updateTemplate(@RequestParam long jobId,@RequestParam int type) {
        return new HttpResult(ciService.updateTemplate(jobId,type));
    }

    @RequestMapping(value = "/template/updates", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult updateTemplates(@RequestParam long id) {
        return new HttpResult(ciService.updateTemplates(id));
    }


    @RequestMapping(value = "/build/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getBuildPage(@RequestParam long jobId, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ciService.getBuildPage(jobId, page, length));
    }

    @RequestMapping(value = "/deploy/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getDeployPage(@RequestParam long jobId, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(ciService.getDeployPage(jobId, page, length));
    }

    @RequestMapping(value = "/artifact/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getArtifactPage(@RequestParam long jobId, @RequestParam int buildNumber) {
        return new HttpResult(ciService.getArtifact(jobId, buildNumber));
    }

    @RequestMapping(value = "/build/commits", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCommits(@RequestParam long jobId,@RequestParam String jobName, @RequestParam String branch) {
        return new HttpResult(ciService.getBuildCommit(jobId,jobName,branch));
    }

    /**
     * 查询服务器分组信息
     *
     * @return
     */
    @RequestMapping(value = "/hostPattern/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getHostPatter(@RequestParam long serverGroupId) {
        return new HttpResult(ciService.getHostPatternCi(serverGroupId),true);
    }

}
