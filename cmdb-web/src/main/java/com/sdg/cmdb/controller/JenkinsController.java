package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;

import com.sdg.cmdb.domain.jenkins.*;

import com.sdg.cmdb.service.JenkinsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/jenkins")
public class JenkinsController {


    @Resource
    private JenkinsService jenkinsService;

    @RequestMapping(value = "/jobNote", method = RequestMethod.POST)
    public HttpResult jenkinsNoti(@RequestBody JobNoteVO jenkinsNoti) {
        return new HttpResult(jenkinsService.jobNotes(jenkinsNoti));

    }

    /**
     * 暂时保留但不使用
     */
    @RequestMapping(value = "/android/jobNote", method = RequestMethod.POST)
    public HttpResult jenkinsAndroidNoti(@RequestBody JobNoteVO jenkinsNoti) {
        System.err.println(jenkinsNoti);
        return new HttpResult(jenkinsService.jobNotes(jenkinsNoti));
    }

    /**
     * 暂时保留但不使用
     */
    @RequestMapping(value = "/ios/jobNote", method = RequestMethod.POST)
    public HttpResult jenkinsIosNoti(@RequestBody JobNoteVO jenkinsNoti) {
        System.err.println(jenkinsNoti);
        return new HttpResult(jenkinsService.jobNotes(jenkinsNoti));
    }

    /**
     * 获取webHooks详情页
     *
     * @param projectName
     * @param repositoryName
     * @param webHooksType
     * @param triggerBuild
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/webHooks/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryWebHooksPage(@RequestParam String projectName, @RequestParam String repositoryName,
                                        @RequestParam int webHooksType, @RequestParam int triggerBuild,
                                        @RequestParam int page, @RequestParam int length) {
        return new HttpResult(jenkinsService.getWebHooksPage(projectName, repositoryName, webHooksType, triggerBuild, page, length));
    }

    @RequestMapping(value = "/jobs/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryJobsPage(@RequestParam String jobName,
                                    @RequestParam int jobEnvType, @RequestParam int buildType,
                                    @RequestParam int page, @RequestParam int length) {
        return new HttpResult(jenkinsService.queryJobsPage(jobName, jobEnvType, buildType, page, length));
    }

    @RequestMapping(value = "/jobs/build", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult jobsBuild(@RequestParam long id) {
        return new HttpResult(jenkinsService.buildJob(id));
    }



    @RequestMapping(value = "/jobs/create", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult jobsCteate(@RequestParam long id) {
        return new HttpResult(jenkinsService.createJob(id));
    }


    @RequestMapping(value = "/jobs/ios/build", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult jobsIosBuild(@RequestParam long id, @RequestParam String mbranch, @RequestParam String buildType) {
        return new HttpResult(jenkinsService.buildJob(id, mbranch, buildType));
    }


    @RequestMapping(value = "/job/refs/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getJobRefs(@RequestParam long id) {
        return new HttpResult(jenkinsService.queryJobRefs(id));
    }


    @RequestMapping(value = "/job/refs/change", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getJobChgRefs(@RequestParam long id,@RequestParam String ref,@RequestParam int type) {
        return new HttpResult(jenkinsService.changeJobRefs(id,ref,type));
    }

    @RequestMapping(value = "/job/refs/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult updateJobRefs(@RequestParam long id) {
        return new HttpResult(jenkinsService.updateJobRefs(id));
    }


    @RequestMapping(value = "/jobs/rebuild", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult jobsRebuild(@RequestParam long id) {
        return new HttpResult(jenkinsService.rebuildJob(id));
    }


    @RequestMapping(value = "/jobs/appLink", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult jobsAppLink(@RequestParam long id) {
        return new HttpResult(jenkinsService.appLink(id));
    }

    @RequestMapping(value = "/jobs/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delJobs(@RequestParam long id) {
        return new HttpResult(jenkinsService.delJob(id));
    }

    @RequestMapping(value = "/jobs/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveJob(@RequestBody JenkinsJobVO jenkinsJobVO) {
        return new HttpResult(jenkinsService.saveJob(jenkinsJobVO));
    }

    @RequestMapping(value = "/jobs/params/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryJobParams(@RequestParam long jobId) {
        return new HttpResult(jenkinsService.queryJobParams(jobId));
    }

    @RequestMapping(value = "/jobs/params/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delJobParams(@RequestParam long id) {
        return new HttpResult(jenkinsService.delJobParams(id));
    }

    @RequestMapping(value = "/jobs/params/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveJobParams(@RequestBody JobParamDO jobParamDO) {
        BusinessWrapper<Boolean> wrapper;
        if (jobParamDO.getId() == 0) {
            wrapper = jenkinsService.addJobParams(jobParamDO);
        } else {
            wrapper = jenkinsService.updateJobParams(jobParamDO);
        }
        if (wrapper.isSuccess()) {
            return new HttpResult(wrapper.getBody());
        } else {
            return new HttpResult(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RequestMapping(value = "/job/builds/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryJobBuildsPage(@RequestParam String jobName,
                                         @RequestParam int buildNumber,
                                         @RequestParam int page, @RequestParam int length) {
        return new HttpResult(jenkinsService.queryJobBuildsPage(jobName, buildNumber, page, length));
    }

    @RequestMapping(value = "/job/builds", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryJobBuilds(@RequestParam long id) {
        return new HttpResult(jenkinsService.queryJobBuilds(id));
    }

    @RequestMapping(value = "/projects/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryJobBuildsPage(@RequestParam String projectName,
                                         @RequestParam String content,
                                         @RequestParam int buildType,
                                         @RequestParam int page, @RequestParam int length) {
        return new HttpResult(jenkinsService.getProjectsPage(projectName, content, buildType, page, length));
    }

    @RequestMapping(value = "/project/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveProject(@RequestBody JenkinsProjectsDO jenkinsProjectsDO) {
        return new HttpResult(jenkinsService.saveProject(jenkinsProjectsDO));
    }

    @RequestMapping(value = "/project/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delProject(@RequestParam long id) {
        return new HttpResult(jenkinsService.delProject(id));
    }


    @RequestMapping(value = "/project/params/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveProjectParam(@RequestBody BaseParamDO baseParamDO) {
        return new HttpResult(jenkinsService.saveProjectParam(baseParamDO));
    }

    @RequestMapping(value = "/project/params/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delProjectParam(@RequestParam long id) {
        return new HttpResult(jenkinsService.delProjectParam(id));
    }

    @RequestMapping(value = "/project/params/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryProjectParams(@RequestParam long id) {
        return new HttpResult(jenkinsService.queryProjectParams(id));
    }


    @RequestMapping(value = "/project/env/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveProjectsEnv(@RequestBody JenkinsProjectsEnvDO jenkinsProjectsEnvDO) {
        return new HttpResult(jenkinsService.saveProjectsEnv(jenkinsProjectsEnvDO));
    }


    @RequestMapping(value = "/project/env/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult saveProjectsEnv(@RequestParam long id, @RequestParam long projectId, @RequestParam int envType) {
        return new HttpResult(jenkinsService.delProjectsEnv(id,projectId,envType));
    }

    @RequestMapping(value = "/project/env/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryProjectsEnv(@RequestParam long id) {
        return new HttpResult(jenkinsService.queryProjectsEnv(id));
    }

    @RequestMapping(value = "/project/env/params/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveProjectsEnvParams(@RequestBody JenkinsProjectsEnvVO jenkinsProjectsEnvVO) {
        return new HttpResult(jenkinsService.saveProjectsEnvParams(jenkinsProjectsEnvVO));
    }


    @RequestMapping(value = "/project/job/save", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult saveProjectJob(@RequestParam long id) {
        return new HttpResult(jenkinsService.saveProjectJob(id));
    }


}
