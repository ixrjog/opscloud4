package com.baiyi.opscloud.controller;

import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogMemberParam;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogVO;
import com.baiyi.opscloud.facade.AliyunLogFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 3:28 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/aliyun/log")
@Api(tags = "阿里云日志服务管理")
public class AliyunLogController {

    @Resource
    private AliyunLogFacade aliyunLogFacade;

    @ApiOperation(value = "查询日志服务配置信息")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunLogVO.Log>> queryLogPage(@RequestBody @Valid AliyunLogParam.PageQuery pageQuery) {
        return new HttpResult<>(aliyunLogFacade.queryLogPage(pageQuery));
    }

    @ApiOperation(value = "推送当前日志服务配置中的服务器组成员")
    @GetMapping(value = "/push", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pushLog(@RequestParam int id) {
        return new HttpResult<>(aliyunLogFacade.pushLog(id));
    }

    @ApiOperation(value = "更新日志服务配置信息")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLog(@RequestBody @Valid AliyunLogVO.Log log) {
        return new HttpResult<>(aliyunLogFacade.updateLog(log));
    }

    @ApiOperation(value = "新增日志服务配置信息")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLog(@RequestBody @Valid AliyunLogVO.Log log) {
        return new HttpResult<>(aliyunLogFacade.addLog(log));
    }

    @ApiOperation(value = "查询日志服务project信息")
    @PostMapping(value = "/project/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<Project>> queryProject(@RequestBody @Valid AliyunLogParam.ProjectQuery query) {
        return new HttpResult<>(aliyunLogFacade.getProjects(query));
    }

    @ApiOperation(value = "查询日志服务logstore信息")
    @PostMapping(value = "/logstore/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> queryLogstore(@RequestBody @Valid AliyunLogParam.LogStoreQuery query) {
        return new HttpResult<>(aliyunLogFacade.getLogStores(query));
    }

    @ApiOperation(value = "查询日志服务config信息")
    @PostMapping(value = "/config/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> queryConfig(@RequestBody @Valid AliyunLogParam.ConfigQuery query) {
        return new HttpResult<>(aliyunLogFacade.getConfigs(query));
    }


    @ApiOperation(value = "查询日志服务配置成员信息")
    @PostMapping(value = "/member/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable< AliyunLogMemberVO.LogMember>> queryLogMemberPage(@RequestBody @Valid AliyunLogMemberParam.PageQuery pageQuery) {
        return new HttpResult<>(aliyunLogFacade.queryLogMemberPage(pageQuery));
    }

    @ApiOperation(value = "新增日志服务配置中的服务器组成员")
    @PostMapping(value = "/member/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLogMember(@RequestBody @Valid AliyunLogMemberParam.AddLogMember addLogMember) {
        return new HttpResult<>(aliyunLogFacade.addLogMember(addLogMember));
    }

    @ApiOperation(value = "推送日志服务配置中的服务器组成员")
    @GetMapping(value = "/member/push", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pushLogMember(@RequestParam int id) {
        return new HttpResult<>(aliyunLogFacade.pushLogMember(id));
    }

    @ApiOperation(value = "移除日志服务配置中的服务器组成员")
    @DeleteMapping(value = "/member/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> removeLogMember(@RequestParam int id) {
        return new HttpResult<>(aliyunLogFacade.removeLogMember(id));
    }

    @ApiOperation(value = "日志服务配置查询服务器组")
    @PostMapping(value = "/member/server/group/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcServerGroup>> queryLogMemberServerGroupPage(@RequestBody @Valid ServerGroupParam.LogMemberServerGroupQuery queryParam) {
        return new HttpResult<>(aliyunLogFacade.queryLogMemberServerGroupPage(queryParam));
    }

}
