package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.workflow.TeamDO;
import com.sdg.cmdb.domain.workflow.TeamuserDO;
import com.sdg.cmdb.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/team")
public class TeamController {

    @Resource
    private TeamService teamService;

    /**
     * 团队分页数据查询
     *
     * @param teamName
     * @param teamType
     * @param teamleaderUsername
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getTeamPage(
            @RequestParam String teamName,
            @RequestParam String teamleaderUsername,
            @RequestParam int teamType,
            @RequestParam int page, @RequestParam int length) {
        return new HttpResult(
                teamService.getTeamPage(teamName, teamleaderUsername,
                        teamType, page, length)
        );
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getTeam(
            @RequestParam long id) {
        return new HttpResult(
                teamService.getTeam(id)
        );
    }

    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delTeam(@RequestParam long id) {
        return new HttpResult(
                teamService.delTeam(id)
        );
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveTeam(@RequestBody TeamDO teamDO) {
        return new HttpResult(
                teamService.saveTeam(teamDO)
        );
    }

    @RequestMapping(value = "/teamuser/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delTeamuser(@RequestParam long id) {
        return new HttpResult(
                teamService.delTeamuser(id)
        );
    }

    @RequestMapping(value = "/teamuser/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveTeamuser(@RequestBody TeamuserDO teamuserDO) {
        return new HttpResult(
                teamService.saveTeamuser(teamuserDO)
        );
    }

}
