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

    private final LeoTemplateFacade templateFacade;

    private final LeoJobFacade jobFacade;

    private final LeoBuildFacade buildFacade;

    private final LeoDeployFacade deployFacade;

    private final LeoBuildPipelineFacade buildPipelineFacade;

    private final LeoRuleFacade ruleFacade;

    private final LeoChartFacade chartFacade;

    // Chart

    @ApiOperation(value = "图表-云词")
    @GetMapping(value = "/chart/keywords/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, Integer>> getLeoChartKeywords() {
        return new HttpResult<>(chartFacade.getKeywords());
    }

    // Template

    @ApiOperation(value = "分页查询任务模板列表")
    @PostMapping(value = "/template/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoTemplateVO.Template>> queryLeoTemplatePage(@RequestBody @Valid LeoTemplateParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(templateFacade.queryLeoTemplatePage(pageQuery));
    }

    @ApiOperation(value = "新增任务模板")
    @PostMapping(value = "/template/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoTemplate(@RequestBody @Valid LeoTemplateParam.AddTemplate addTemplate) {
        templateFacade.addLeoTemplate(addTemplate);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新任务模板")
    @PutMapping(value = "/template/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoTemplate(@RequestBody @Valid LeoTemplateParam.UpdateTemplate updateTemplate) {
        templateFacade.updateLeoTemplate(updateTemplate);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "从JenkinsJob更新模板内容")
    @PutMapping(value = "/template/content/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoTemplateVO.Template> updateLeoTemplateContent(@RequestBody @Valid LeoTemplateParam.UpdateTemplate updateTemplate) {
        return new HttpResult<>(templateFacade.updateLeoTemplateContent(updateTemplate));
    }

    @ApiOperation(value = "删除指定的任务模板")
    @DeleteMapping(value = "/template/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoTemplateById(@RequestParam @Valid int templateId) {
        templateFacade.deleteLeoTemplateById(templateId);
        return HttpResult.SUCCESS;
    }

    // Job

    @ApiOperation(value = "分页查询任务列表")
    @PostMapping(value = "/job/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoJobVO.Job>> queryLeoJobPage(@RequestBody @Valid LeoJobParam.JobPageQuery pageQuery) {
        return new HttpResult<>(jobFacade.queryLeoJobPage(pageQuery));
    }

    @ApiOperation(value = "新增任务")
    @PostMapping(value = "/job/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoJob(@RequestBody @Valid LeoJobParam.AddJob addJob) {
        jobFacade.addLeoJob(addJob);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新任务")
    @PutMapping(value = "/job/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoJob(@RequestBody @Valid LeoJobParam.UpdateJob updateJob) {
        jobFacade.updateLeoJob(updateJob);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新任务模板内容")
    @PutMapping(value = "/job/template/content/upgrade", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> upgradeLeoJobTemplateContent(@RequestParam int jobId) {
        jobFacade.upgradeLeoJobTemplateContent(jobId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的任务")
    @DeleteMapping(value = "/job/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoJobById(@RequestParam @Valid int jobId) {
        jobFacade.deleteLeoJobById(jobId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询任务构建历史")
    @PostMapping(value = "/job/build/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoBuildVO.Build>> queryLeoJobBuildPage(@RequestBody @Valid LeoJobParam.JobBuildPageQuery pageQuery) {
        return new HttpResult<>(buildFacade.queryLeoJobBuildPage(pageQuery));
    }

    @ApiOperation(value = "分页查询任务部署历史")
    @PostMapping(value = "/job/deploy/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoDeployVO.Deploy>> queryLeoJobDeployPage(@RequestBody @Valid LeoJobParam.JobDeployPageQuery pageQuery) {
        return new HttpResult<>(deployFacade.queryLeoJobDeployPage(pageQuery));
    }

    @ApiOperation(value = "创建CR仓库")
    @PutMapping(value = "/job/cr/repository/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createCrRepositoryWithLeoJobId(@RequestParam @Valid int jobId) {
        jobFacade.createCrRepositoryWithLeoJobId(jobId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "从应用复制任务")
    @PostMapping(value = "/job/clone", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> cloneJob(@RequestBody @Valid LeoJobParam.CloneJob cloneJob) {
        jobFacade.cloneJob(cloneJob);
        return HttpResult.SUCCESS;
    }

    // Build

    @ApiOperation(value = "执行构建")
    @PostMapping(value = "/build/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> doLeoBuild(@RequestBody @Valid LeoBuildParam.DoBuild doBuild) {
        buildFacade.doBuild(doBuild);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "停止构建")
    @PutMapping(value = "/build/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> stopLeoBuild(@RequestParam @Valid int buildId) {
        buildFacade.stopBuild(buildId);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "构建分支选项")
    @PostMapping(value = "/build/branch/options/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.BranchOptions> getBuildBranchOptions(@RequestBody @Valid LeoBuildParam.GetBuildBranchOptions getOptions) {
        return new HttpResult<>(buildFacade.getBuildBranchOptions(getOptions));
    }

    @ApiOperation(value = "创建默认分支")
    @PostMapping(value = "/build/branch/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.BranchOptions> createBuildBranch(@RequestBody @Valid LeoBuildParam.CreateBuildBranch createBuildBranch) {
        return new HttpResult<>(buildFacade.createBuildBranch(createBuildBranch));
    }

    @ApiOperation(value = "更新构建详情")
    @PutMapping(value = "/build/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoBuild(@RequestBody @Valid LeoBuildParam.UpdateBuild updateBuild) {
        buildFacade.updateLeoBuild(updateBuild);
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
        deployFacade.doDeploy(doDeploy);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "查询部署版本")
    @PostMapping(value = "/deploy/version/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoBuildVO.Build>> queryLeoDeployVersion(@RequestBody @Valid LeoDeployParam.QueryDeployVersion queryBuildVersion) {
        return new HttpResult<>(deployFacade.queryLeoDeployVersion(queryBuildVersion));
    }

    @ApiOperation(value = "查询部署无状态")
    @PostMapping(value = "/deploy/deployment/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ApplicationResourceVO.BaseResource>> queryLeoDeployDeployment(@RequestBody @Valid LeoDeployParam.QueryDeployDeployment queryDeployDeployment) {
        return new HttpResult<>(deployFacade.queryLeoBuildDeployment(queryDeployDeployment));
    }

    @ApiOperation(value = "克隆部署无状态")
    @PostMapping(value = "/deploy/deployment/clone", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> cloneLeoDeployDeployment(@RequestBody @Valid LeoDeployParam.CloneDeployDeployment cloneDeployDeployment) {
        deployFacade.cloneDeployDeployment(cloneDeployDeployment);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "停止部署(逻辑层)")
    @PutMapping(value = "/deploy/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> stopLeoDeploy(@RequestParam @Valid int deployId) {
        deployFacade.stopDeploy(deployId);
        return HttpResult.SUCCESS;
    }

    // Pipeline

    @ApiOperation(value = "查询流水线节点步骤")
    @PostMapping(value = "/pipeline/node/steps/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoBuildPipelineVO.Step>> getPipelineRunNodeSteps(@RequestBody @Valid LeoBuildPipelineParam.GetPipelineRunNodeSteps param) {
        return new HttpResult<>(buildPipelineFacade.getPipelineRunNodeSteps(param));
    }

    // Rule

    @ApiOperation(value = "分页查询规则配置列表")
    @PostMapping(value = "/rule/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoRuleVO.Rule>> queryLeoRulePage(@RequestBody @Valid LeoRuleParam.RulePageQuery pageQuery) {
        return new HttpResult<>(ruleFacade.queryLeoRulePage(pageQuery));
    }

    @ApiOperation(value = "更新规则配置")
    @PutMapping(value = "/rule/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoRule(@RequestBody @Valid LeoRuleParam.UpdateRule updateRule) {
        ruleFacade.updateLeoRule(updateRule);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "新增规则配置")
    @PostMapping(value = "/rule/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoRule(@RequestBody @Valid LeoRuleParam.AddRule addRule) {
        ruleFacade.addLeoRule(addRule);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的规则配置")
    @DeleteMapping(value = "/rule/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoRuleById(@RequestParam @Valid int ruleId) {
        ruleFacade.deleteLeoRuleById(ruleId);
        return HttpResult.SUCCESS;
    }

}
