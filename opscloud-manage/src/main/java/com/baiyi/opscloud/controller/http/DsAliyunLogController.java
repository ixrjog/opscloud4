package com.baiyi.opscloud.controller.http;

import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogMemberParam;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogParam;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogVO;
import com.baiyi.opscloud.facade.datasource.aliyun.AliyunLogFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/17 2:58 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/datasource/aliyun/log")
@Api(tags = "数据源实例-阿里云日志服务")
@RequiredArgsConstructor
public class DsAliyunLogController {

    private final AliyunLogFacade aliyunLogFacade;

    @ApiOperation(value = "分页查询阿里云日志服务列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunLogVO.Log>> queryLogPage(@RequestBody @Valid AliyunLogParam.AliyunLogPageQuery pageQuery) {
        return new HttpResult<>(aliyunLogFacade.queryLogPage(pageQuery));
    }

    @ApiOperation(value = "新增阿里云日志服务")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLog(@RequestBody @Valid AliyunLogVO.Log log) {
        aliyunLogFacade.addLog(log);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "推送阿里云日志服务配置")
    @PutMapping(value = "/push", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pushLogById(@RequestParam @Valid int id) {
        aliyunLogFacade.pushLogById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新阿里云日志服务")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLog(@RequestBody @Valid AliyunLogVO.Log log) {
        aliyunLogFacade.updateLog(log);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的阿里云日志服务")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLogById(@RequestParam int id) {
        aliyunLogFacade.deleteLogById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询阿里云日志服务项目列表")
    @PostMapping(value = "/project/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<Project>> queryProject(@RequestBody @Valid AliyunLogParam.ProjectQuery query) {
        return new HttpResult<>(aliyunLogFacade.queryProject(query));
    }

    @ApiOperation(value = "查询阿里云日志服务日志库列表")
    @PostMapping(value = "/logstore/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> queryLogstore(@RequestBody @Valid AliyunLogParam.LogStoreQuery query) {
        return new HttpResult<>(aliyunLogFacade.queryLogstore(query));
    }

    @ApiOperation(value = "查询阿里云日志服务配置列表")
    @PostMapping(value = "/config/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> queryLogstore(@RequestBody @Valid AliyunLogParam.ConfigQuery query) {
        return new HttpResult<>(aliyunLogFacade.queryConfig(query));
    }

    @ApiOperation(value = "分页查询阿里云日志服务成员列表")
    @PostMapping(value = "/member/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunLogMemberVO.LogMember>> queryLogMemberPage(@RequestBody @Valid AliyunLogMemberParam.LogMemberPageQuery pageQuery) {
        return new HttpResult<>(aliyunLogFacade.queryLogMemberPage(pageQuery));
    }

    @ApiOperation(value = "新增阿里云日志服务成员")
    @PostMapping(value = "/member/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLogMember(@RequestBody @Valid AliyunLogMemberVO.LogMember logMember) {
        aliyunLogFacade.addLogMember(logMember);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新阿里云日志服务成员")
    @PutMapping(value = "/member/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLogMember(@RequestBody @Valid AliyunLogMemberVO.LogMember logMember) {
        aliyunLogFacade.updateLogMember(logMember);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的阿里云日志服务成员")
    @DeleteMapping(value = "/member/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLogMemberById(@RequestParam int id) {
        aliyunLogFacade.deleteLogMemberById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "推送阿里云日志服务成员配置")
    @PutMapping(value = "/member/push", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pushLogMemberById(@RequestParam @Valid int id) {
        aliyunLogFacade.pushLogMemberById(id);
        return HttpResult.SUCCESS;
    }

}
