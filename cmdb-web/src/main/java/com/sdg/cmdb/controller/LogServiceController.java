package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.logService.LogFormatDefault;
import com.sdg.cmdb.domain.logService.LogHistogramsVO;
import com.sdg.cmdb.domain.logService.LogServiceGroupCfgDO;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceDefaultQuery;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogserviceNginxQuery;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceServerGroupCfgVO;
import com.sdg.cmdb.service.AliyunLogManageService;
import com.sdg.cmdb.service.AliyunLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/logService")
public class LogServiceController {


    @Resource
    private AliyunLogService aliyunLogService;

    @Resource
    private AliyunLogManageService aliyunLogManageService;

    /**
     * 获取指定条件的服务器列表分页数据
     *
     * @param page
     * @param length
     * @param serverName
     * @return
     */
    @RequestMapping(value = "/nginx/cfg/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryLogServiceCfgPage(
            @RequestParam int page, @RequestParam int length, @RequestParam String serverName) {

        return new HttpResult(aliyunLogService.getLogServiceCfgPage(page, length, serverName));
    }


    @RequestMapping(value = "/logHistograms/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryNginxLogHistogramsPage(@RequestParam long logServiceId,
                                                  @RequestParam int page, @RequestParam int length) {
        return new HttpResult(aliyunLogService.getLogHistogramsPage(logServiceId, page, length));
    }


    /**
     * 查询nginx日志视图
     *
     * @param logserviceNginxQuery
     * @return
     */
    @RequestMapping(value = "/nginx/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveServer(@RequestBody LogserviceNginxQuery logserviceNginxQuery) {
        try {
            return new HttpResult(aliyunLogService.queryLog(logserviceNginxQuery), true);
        } catch (Exception e) {
            return new HttpResult(null);
        }
    }

    /**
     * 查看日志详情(重新封装同时支持 ka/www 2种类型的nginx日志)
     *
     * @param logHistogramsVO
     * @return
     */
    @RequestMapping(value = "/nginx/viewLog", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult nginxViewlog(@RequestBody LogHistogramsVO logHistogramsVO) {
        try {
            return new HttpResult(aliyunLogService.queryNginxLog(logHistogramsVO), true);
        } catch (Exception e) {
            return new HttpResult(null);
        }
    }

    @RequestMapping(value = "/java/viewLog", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult javaViewlog(@RequestBody LogHistogramsVO logHistogramsVO) {
        try {
            TableVO<List<LogFormatDefault>> tableVO = aliyunLogService.queryDefaultLog(logHistogramsVO);
            return new HttpResult(tableVO);
        } catch (Exception e) {
            return new HttpResult(null);
        }
    }


    @RequestMapping(value = "/java/path/query/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryLogServicePathPage(
            @RequestParam int page, @RequestParam int length, @RequestParam String tagPath, @RequestParam long serverGroupId) {
        return new HttpResult(aliyunLogService.getLogServicePathPage(page, length, tagPath, serverGroupId));
    }


    /**
     * 初始化视图
     *
     * @param logServiceDefaultQuery
     * @return
     */
    @RequestMapping(value = "/java/query", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveServer(@RequestBody LogServiceDefaultQuery logServiceDefaultQuery) {
        try {
            return new HttpResult(aliyunLogService.queryLog(logServiceDefaultQuery));
        } catch (Exception e) {
            return new HttpResult(null);
        }
    }

    /**
     * 查询服务器组的分页数据
     *
     * @param page
     * @param length
     * @param name
     * @return
     */
    @RequestMapping(value = "/servergroup/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getServerGroupPage(@RequestParam int page, @RequestParam int length,
                                         @RequestParam String name, @RequestParam boolean isUsername, @RequestParam int useType) {
        return new HttpResult(aliyunLogService.queryServerGroupPage(page, length, name, isUsername, useType));
    }


    /**
     * 查询日志服务所有project
     *
     * @return
     */
    @RequestMapping(value = "/project/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLogServiceProject() {
        return new HttpResult(aliyunLogManageService.queryListProject());
    }


    /**
     * 查询日志服务所有project
     *
     * @return
     */
    @RequestMapping(value = "/logstore/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLogServiceLogStore(@RequestParam String project) {
        return new HttpResult(aliyunLogManageService.queryListLogStores(project));
    }

    @RequestMapping(value = "/config/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLogServiceConfig(@RequestParam String project ,@RequestParam String logstore) {
        return new HttpResult(aliyunLogManageService.queryListConfig(project,logstore));
    }


    @RequestMapping(value = "/machineGroup/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getMachineGroup(@RequestParam String project, @RequestParam String groupName) {
        return new HttpResult(aliyunLogManageService.getMachineGroupVO(project, groupName));
    }


    @RequestMapping(value = "/serverGroupCfg/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveServerGroupCfg(@RequestBody LogServiceServerGroupCfgVO logServiceServerGroupCfgVO) {
        return new HttpResult(aliyunLogManageService.saveServerGroupCfg(logServiceServerGroupCfgVO));
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult statusLogService() {
        return new HttpResult(aliyunLogManageService.logServiceStatus());
    }

    @RequestMapping(value = "/group/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryLogServiceGroupPage(@RequestParam String project, @RequestParam String logstore, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(aliyunLogManageService.getLogServiceGroupPage(project, logstore, page, length));
    }

    @RequestMapping(value = "/group/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getLogServiceGroup(@RequestParam long id) {
        return new HttpResult(aliyunLogManageService.getLogServiceGroup(id));
    }

    @RequestMapping(value = "/group/addMember", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addLogServiceGroupMember(@RequestParam long groupCfgId, @RequestParam long serverGroupId) {
        return new HttpResult(aliyunLogManageService.addMember(groupCfgId, serverGroupId));
    }

    @RequestMapping(value = "/group/delMember", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delLogServiceGroupMember(@RequestParam long id) {
        return new HttpResult(aliyunLogManageService.delMember(id));
    }

    @RequestMapping(value = "/group/pushCfg", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult pushGroupCfg(@RequestParam long id) {
        return new HttpResult(aliyunLogManageService.pushGroupCfg(id));
    }

    @RequestMapping(value = "/group/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveGroupCfg(@RequestBody LogServiceGroupCfgDO logServiceGroupCfgDO) {
        return new HttpResult(aliyunLogManageService.saveGroupCfg(logServiceGroupCfgDO));
    }

}
