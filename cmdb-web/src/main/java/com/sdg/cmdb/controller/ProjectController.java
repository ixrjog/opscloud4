package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.projectManagement.ProjectManagementVO;
import com.sdg.cmdb.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryProjectManagementPage(@RequestParam String projectName, @RequestParam int projectType,
                                                 @RequestParam int status,
                                                 @RequestParam String leaderUsername,
                                                 @RequestParam int page, @RequestParam int length) {
        return new HttpResult(projectService.queryProjectManagementPage(projectName, projectType, status, leaderUsername, page, length));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getProjectManagement(@RequestParam long id) {
        return new HttpResult(projectService.getProjectManagement(id));
    }

    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delProjectManagement(@RequestParam long pmId) {
        return new HttpResult(projectService.delProjectManagement(pmId));
    }


    @RequestMapping(value = "/heartbeat/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryProjectHeartbeatPage(@RequestParam String projectName,
                                                @RequestParam int projectType,
                                                @RequestParam int status,
                                                @RequestParam String leaderUsername,
                                                @RequestParam int page, @RequestParam int length) {
        return new HttpResult(projectService.queryHeartbeatPage(projectName, projectType, status, leaderUsername, page, length));
    }

    /**
     * 保存指定的serveritem
     *
     * @param pmVO
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveServer(@RequestBody ProjectManagementVO pmVO) {
        return new HttpResult(projectService.saveProjectManagement(pmVO));
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addUser(@RequestParam long pmId, @RequestParam long userId) {
        return new HttpResult(projectService.addUser(pmId, userId));
    }

    @RequestMapping(value = "/user/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delUser(@RequestParam long id) {
        return new HttpResult(projectService.delUser(id));
    }

    @RequestMapping(value = "/serverGroup/add", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addServerGroup(@RequestParam long pmId, @RequestParam long serverGroupId) {
        return new HttpResult(projectService.addServerGroup(pmId, serverGroupId));
    }

    @RequestMapping(value = "/serverGroup/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delServerGroup(@RequestParam long id) {
        return new HttpResult(projectService.delServerGroup(id));
    }

    @RequestMapping(value = "/heartbeat/save", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult saveHeartbeat(@RequestParam long pmId, @RequestParam int status) {
        return new HttpResult(projectService.saveHeartbeat(pmId, status));
    }

}
