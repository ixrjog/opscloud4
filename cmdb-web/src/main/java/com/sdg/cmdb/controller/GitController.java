package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksVO;
import com.sdg.cmdb.service.GitService;
import com.sdg.cmdb.service.GitlabService;
import com.sdg.cmdb.service.impl.GitServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/git")
public class GitController {

    @Resource
    private GitlabService gitlabService;


    @Resource
    private GitService gitService;


    @RequestMapping(value = "/webHooks", method = RequestMethod.POST)
    public HttpResult forTriger(@RequestBody GitlabWebHooksVO webhooks) {
        return new HttpResult(gitlabService.webHooks(webhooks));
    }


    @RequestMapping(value = "/refs/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getJobRefs(@RequestParam String project, @RequestParam String repo) {
        return new HttpResult(gitService.queryRefs(GitServiceImpl.STASH_REPOSITORY, project, repo));
    }

    @RequestMapping(value = "/refs/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult updateJobRefs(@RequestParam String project, @RequestParam String repo) {

        return new HttpResult(gitService.getRefs(GitServiceImpl.STASH_REPOSITORY, project, repo));
    }


}
