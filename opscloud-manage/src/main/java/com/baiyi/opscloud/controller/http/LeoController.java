package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.leo.*;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.leo.*;
import com.baiyi.opscloud.facade.leo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/11/1 18:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/leo")
@Api(tags = "Leo管理")
@RequiredArgsConstructor
public class LeoController {

    private final LeoTemplateFacade leoTemplateFacade;

    private final LeoJobFacade leoJobFacade;

    private final LeoBuildFacade leoBuildFacade;

    private final LeoDeployFacade leoDeployFacade;

    private final LeoBuildPipelineFacade leoBuildPipelineFacade;

    private final LeoRuleFacade leoRuleFacade;

    private final LeoChartFacade leoChartFacade;

    @ApiOperation(value = "图表-云词")
    @GetMapping(value = "/chart/keywords/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, Integer>> getLeoChartKeywords() {
        return new HttpResult<>(leoChartFacade.getKeywords());
    }

    // Template -------------

    @ApiOperation(value = "分页查询任务模板列表")
    @PostMapping(value = "/template/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoTemplateVO.Template>> queryLeoTemplatePage(@RequestBody @Valid LeoTemplateParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(leoTemplateFacade.queryLeoTemplatePage(pageQuery));
    }

    @ApiOperation(value = "新增任务模板")
    @PostMapping(value = "/template/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoTemplate(@RequestBody @Valid LeoTemplateParam.AddTemplate addTemplate) {
        leoTemplateFacade.addLeoTemplate(addTemplate);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新任务模板")
    @PutMapping(value = "/template/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoTemplate(@RequestBody @Valid LeoTemplateParam.UpdateTemplate updateTemplate) {
        leoTemplateFacade.updateLeoTemplate(updateTemplate);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "从JenkinsJob更新模板内容")
    @PutMapping(value = "/template/content/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoTemplateVO.Template> updateLeoTemplateContent(@RequestBody @Valid LeoTemplateParam.UpdateTemplate updateTemplate) {
        return new HttpResult<>(leoTemplateFacade.updateLeoTemplateContent(updateTemplate));
    }

    @ApiOperation(value = "删除指定的任务模板")
    @DeleteMapping(value = "/template/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoTemplateById(@RequestParam @Valid int templateId) {
        leoTemplateFacade.deleteLeoTemplateById(templateId);
        return HttpResult.SUCCESS;
    }

    // Job

    @ApiOperation(value = "分页查询任务列表")
    @PostMapping(value = "/job/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoJobVO.Job>> queryLeoJobPage(@RequestBody @Valid LeoJobParam.JobPageQuery pageQuery) {
        return new HttpResult<>(leoJobFacade.queryLeoJobPage(pageQuery));
    }

    @ApiOperation(value = "新增任务")
    @PostMapping(value = "/job/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoJob(@RequestBody @Valid LeoJobParam.AddJob addJob) {
        leoJobFacade.addLeoJob(addJob);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新任务")
    @PutMapping(value = "/job/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoJob(@RequestBody @Valid LeoJobParam.UpdateJob updateJob) {
        leoJobFacade.updateLeoJob(updateJob);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新任务模板内容")
    @PutMapping(value = "/job/template/content/upgrade", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> upgradeLeoJobTemplateContent(@RequestParam int jobId) {
        leoJobFacade.upgradeLeoJobTemplateContent(jobId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的任务")
    @DeleteMapping(value = "/job/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoJobById(@RequestParam @Valid int jobId) {
        leoJobFacade.deleteLeoJobById(jobId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询任务构建历史")
    @PostMapping(value = "/job/build/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoBuildVO.Build>> queryLeoJobBuildPage(@RequestBody @Valid LeoJobParam.JobBuildPageQuery pageQuery) {
        return new HttpResult<>(leoBuildFacade.queryLeoJobBuildPage(pageQuery));
    }

    @ApiOperation(value = "分页查询任务部署历史")
    @PostMapping(value = "/job/deploy/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoDeployVO.Deploy>> queryLeoJobDeployPage(@RequestBody @Valid LeoJobParam.JobDeployPageQuery pageQuery) {
        return new HttpResult<>(leoDeployFacade.queryLeoJobDeployPage(pageQuery));
    }

    // Build

    @ApiOperation(value = "执行构建")
    @PostMapping(value = "/build/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> doLeoBuild(@RequestBody @Valid LeoBuildParam.DoBuild doBuild) {
        leoBuildFacade.doBuild(doBuild);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "停止构建")
    @PutMapping(value = "/build/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> stopLeoBuild(@RequestParam @Valid int buildId) {
        leoBuildFacade.stopBuild(buildId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "构建分支选项")
    @PostMapping(value = "/build/branch/options/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.BranchOptions> getBuildBranchOptions(@RequestBody @Valid LeoBuildParam.GetBuildBranchOptions getOptions) {
        return new HttpResult<>(leoBuildFacade.getBuildBranchOptions(getOptions));
    }

    @ApiOperation(value = "创建默认分支")
    @PostMapping(value = "/build/branch/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.BranchOptions> createBuildBranch(@RequestBody @Valid LeoBuildParam.CreateBuildBranch createBuildBranch) {
        return new HttpResult<>(leoBuildFacade.createBuildBranch(createBuildBranch));
    }

    @ApiOperation(value = "更新构建详情")
    @PutMapping(value = "/build/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoBuild(@RequestBody @Valid LeoBuildParam.UpdateBuild updateBuild) {
        leoBuildFacade.updateLeoBuild(updateBuild);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的构建信息")
    @DeleteMapping(value = "/build/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoBuildById(@RequestParam @Valid int buildId) {
        return HttpResult.SUCCESS;
    }

    // Deploy

    @ApiOperation(value = "执行部署")
    @PostMapping(value = "/deploy/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> doLeoDeploy(@RequestBody @Valid LeoDeployParam.DoDeploy doDeploy) {
        leoDeployFacade.doDeploy(doDeploy);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询部署版本")
    @PostMapping(value = "/deploy/version/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoBuildVO.Build>> queryLeoDeployVersion(@RequestBody @Valid LeoBuildParam.QueryDeployVersion queryBuildVersion) {
        return new HttpResult<>(leoDeployFacade.queryLeoDeployVersion(queryBuildVersion));
    }

    @ApiOperation(value = "查询部署Deployment")
    @PostMapping(value = "/deploy/deployment/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ApplicationResourceVO.BaseResource>> queryLeoDeployDeployment(@RequestBody @Valid LeoBuildParam.QueryDeployDeployment queryDeployDeployment) {
        return new HttpResult<>(leoDeployFacade.queryLeoBuildDeployment(queryDeployDeployment));
    }

    // Pipeline

    @ApiOperation(value = "查询流水线节点步骤")
    @PostMapping(value = "/pipeline/node/steps/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoBuildPipelineVO.Step>> getPipelineRunNodeSteps(@RequestBody @Valid LeoBuildPipelineParam.GetPipelineRunNodeSteps param) {
        return new HttpResult<>(leoBuildPipelineFacade.getPipelineRunNodeSteps(param));
    }

    // Rule
    @ApiOperation(value = "分页查询规则配置列表")
    @PostMapping(value = "/rule/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoRuleVO.Rule>> queryLeoRulePage(@RequestBody @Valid LeoRuleParam.RulePageQuery pageQuery) {
        return new HttpResult<>(leoRuleFacade.queryLeoRulePage(pageQuery));
    }

    @ApiOperation(value = "更新规则配置")
    @PutMapping(value = "/rule/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoRule(@RequestBody @Valid LeoRuleParam.UpdateRule updateRule) {
        leoRuleFacade.updateLeoRule(updateRule);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "新增规则配置")
    @PostMapping(value = "/rule/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoRule(@RequestBody @Valid LeoRuleParam.AddRule addRule) {
        leoRuleFacade.addLeoRule(addRule);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的规则配置")
    @DeleteMapping(value = "/rule/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoRuleById(@RequestParam @Valid int ruleId) {
        leoRuleFacade.deleteLeoRuleById(ruleId);
        return HttpResult.SUCCESS;
    }

}
