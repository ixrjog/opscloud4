package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.param.leo.*;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoBuildRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoDeployRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoDeploymentVersionDetailsRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoJobRequestParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.leo.*;
import com.baiyi.opscloud.facade.leo.*;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/11/1 18:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/leo")
@Tag(name = "Leo管理")
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

    @Operation(summary = "图表-云词")
    @GetMapping(value = "/chart/keywords/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, Integer>> getLeoChartKeywords() {
        return new HttpResult<>(chartFacade.getKeywords());
    }

    // Template

    @Operation(summary = "分页查询任务模板列表")
    @PostMapping(value = "/template/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoTemplateVO.Template>> queryLeoTemplatePage(@RequestBody @Valid LeoTemplateParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(templateFacade.queryLeoTemplatePage(pageQuery));
    }

    @Operation(summary = "新增任务模板")
    @PostMapping(value = "/template/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoTemplate(@RequestBody @Valid LeoTemplateParam.AddTemplate addTemplate) {
        templateFacade.addLeoTemplate(addTemplate);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新任务模板")
    @PutMapping(value = "/template/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoTemplate(@RequestBody @Valid LeoTemplateParam.UpdateTemplate updateTemplate) {
        templateFacade.updateLeoTemplate(updateTemplate);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "从JenkinsJob更新模板内容")
    @PutMapping(value = "/template/content/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoTemplateVO.Template> updateLeoTemplateContent(@RequestBody @Valid LeoTemplateParam.UpdateTemplate updateTemplate) {
        return new HttpResult<>(templateFacade.updateLeoTemplateContent(updateTemplate));
    }

    @Operation(summary = "上传模板到Jenkins")
    @PutMapping(value = "/template/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> uploadTemplate(@RequestParam @Valid int templateId) {
        templateFacade.uploadTemplate(templateId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的任务模板")
    @DeleteMapping(value = "/template/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoTemplateById(@RequestParam @Valid int templateId) {
        templateFacade.deleteLeoTemplateById(templateId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "关联的任务模板升级")
    @PutMapping(value = "/template/job/upgrade", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> upgradeLeoJobTemplate(@RequestBody @Valid LeoTemplateParam.UpgradeJobTemplate upgradeJobTemplate) {
        templateFacade.upgradeLeoJobTemplate(upgradeJobTemplate.getTemplateId());
        return HttpResult.SUCCESS;
    }

    // Job

    @Operation(summary = "分页查询任务列表")
    @PostMapping(value = "/job/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoJobVO.Job>> queryLeoJobPage(@RequestBody @Valid LeoJobParam.JobPageQuery pageQuery) {
        return new HttpResult<>(jobFacade.queryLeoJobPage(pageQuery));
    }

    @Operation(summary = "构建页面分页查询我的任务列表")
    @PostMapping(value = "/job/my/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoJobVO.Job>> queryMyLeoJobPage(@RequestBody @Valid SubscribeLeoJobRequestParam pageQuery) {
        return new HttpResult<>(jobFacade.queryMyLeoJobPage(pageQuery));
    }

    @Operation(summary = "新增任务")
    @PostMapping(value = "/job/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoJob(@RequestBody @Valid LeoJobParam.AddJob addJob) {
        jobFacade.addLeoJob(addJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新任务")
    @PutMapping(value = "/job/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoJob(@RequestBody @Valid LeoJobParam.UpdateJob updateJob) {
        jobFacade.updateLeoJob(updateJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新任务模板内容")
    @PutMapping(value = "/job/template/content/upgrade", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> upgradeLeoJobTemplateContent(@RequestParam int jobId) {
        jobFacade.upgradeLeoJobTemplateContent(jobId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的任务")
    @DeleteMapping(value = "/job/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoJobById(@RequestParam @Valid int jobId) {
        jobFacade.deleteLeoJobById(jobId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "分页查询任务构建历史")
    @PostMapping(value = "/job/build/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoBuildVO.Build>> queryLeoJobBuildPage(@RequestBody @Valid LeoJobParam.JobBuildPageQuery pageQuery) {
        return new HttpResult<>(buildFacade.queryLeoJobBuildPage(pageQuery));
    }

    @Operation(summary = "分页查询我的构建任务")
    @PostMapping(value = "/job/my/build/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoBuildVO.Build>> queryMyLeoJobBuildPage(@RequestBody @Valid SubscribeLeoBuildRequestParam pageQuery) {
        return new HttpResult<>(buildFacade.queryMyLeoJobBuildPage(pageQuery));
    }

    @Operation(summary = "分页查询任务部署历史")
    @PostMapping(value = "/job/deploy/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoDeployVO.Deploy>> queryLeoJobDeployPage(@RequestBody @Valid LeoJobParam.JobDeployPageQuery pageQuery) {
        return new HttpResult<>(deployFacade.queryLeoJobDeployPage(pageQuery));
    }

    @Deprecated
    @Operation(summary = "分页查询我的部署")
    @PostMapping(value = "/job/my/deploy/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoDeployVO.Deploy>> queryMyLeoJobDeployPage(@RequestBody @Valid SubscribeLeoDeployRequestParam pageQuery) {
        return new HttpResult<>(deployFacade.queryMyLeoJobDeployPage(pageQuery));
    }

    @Operation(summary = "查询我的Deployment版本详情")
    @PostMapping(value = "/job/my/deployment/version/details/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoJobVersionVO.JobVersion>> queryMyLeoJobVersion(@RequestBody @Valid SubscribeLeoDeploymentVersionDetailsRequestParam queryParam) {
        return new HttpResult<>(deployFacade.queryMyLeoJobVersion(queryParam));
    }

    @Operation(summary = "创建CR仓库")
    @PutMapping(value = "/job/cr/repository/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createCrRepositoryWithLeoJobId(@RequestParam @Valid int jobId) {
        jobFacade.createCrRepositoryWithLeoJobId(jobId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "从应用复制所有任务")
    @PostMapping(value = "/job/clone", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> cloneJob(@RequestBody @Valid LeoJobParam.CloneJob cloneJob) {
        jobFacade.cloneJob(cloneJob);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "从任务复制任务")
    @PostMapping(value = "/job/one/clone", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> cloneOneJob(@RequestBody @Valid LeoJobParam.CloneOneJob cloneOneJob) {
        jobFacade.cloneOneJob(cloneOneJob);
        return HttpResult.SUCCESS;
    }

    // Build

    @Operation(summary = "执行构建")
    @PostMapping(value = "/build/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuild> doLeoBuild(@RequestBody @Valid LeoBuildParam.DoBuild doBuild) {
        return new HttpResult<>(buildFacade.doBuild(doBuild));
    }

    @Operation(summary = "关闭构建")
    @PutMapping(value = "/build/close", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> closeLeoBuild(@RequestParam @Valid int buildId) {
        buildFacade.closeBuild(buildId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "停止构建")
    @PutMapping(value = "/build/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> stopLeoBuild(@RequestParam @Valid int buildId) {
        buildFacade.stopBuild(buildId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "构建分支选项")
    @PostMapping(value = "/build/branch/options/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.BranchOptions> getBuildBranchOptions(@RequestBody @Valid LeoBuildParam.GetBuildBranchOptions getOptions) {
        return new HttpResult<>(buildFacade.getBuildBranchOptions(getOptions));
    }

    @Operation(summary = "比较分支")
    @PostMapping(value = "/build/branch/compare", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.CompareResults> compareBranch(@RequestBody @Valid LeoBuildParam.CompareBranch compareBranch) {
        return new HttpResult<>(buildFacade.compareBranch(compareBranch));
    }

    @Operation(summary = "查询MavenPublish信息")
    @PostMapping(value = "/build/maven/publish/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.MavenPublishInfo> getBuildMavenPublishInfo(@RequestBody @Valid LeoBuildParam.GetBuildMavenPublishInfo getBuildMavenPublishInfo) {
        return new HttpResult<>(buildFacade.getBuildMavenPublishInfo(getBuildMavenPublishInfo));
    }

    @Operation(summary = "创建默认分支")
    @PostMapping(value = "/build/branch/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.BranchOptions> createBuildBranch(@RequestBody @Valid LeoBuildParam.CreateBuildBranch createBuildBranch) {
        return new HttpResult<>(buildFacade.createBuildBranch(createBuildBranch));
    }

    @Operation(summary = "更新构建详情")
    @PutMapping(value = "/build/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoBuild(@RequestBody @Valid LeoBuildParam.UpdateBuild updateBuild) {
        buildFacade.updateLeoBuild(updateBuild);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询构建详情")
    @GetMapping(value = "/build/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoBuildVO.Build> getLeoBuild(@RequestParam @Valid int buildId) {
        return new HttpResult<>(buildFacade.getLeoBuild(buildId));
    }

    @Operation(summary = "删除指定的构建信息")
    @DeleteMapping(value = "/build/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoBuildById(@RequestParam @Valid int buildId) {
        // TODO
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query all deploys associated with build")
    @GetMapping(value = "/deploys/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoDeployVO.Deploy>> getLeoDeploys(@RequestParam int buildId) {
        return new HttpResult<>(deployFacade.getLeoDeploys(buildId));
    }

    @Operation(summary = "执行部署")
    @PostMapping(value = "/deploy/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoDeploy> doLeoDeploy(@RequestBody @Valid LeoDeployParam.DoDeploy doDeploy) {
        doDeploy.setAutoDeploy(false);
        return new HttpResult<>(deployFacade.doDeploy(doDeploy));
    }

    @Operation(summary = "查询部署版本")
    @PostMapping(value = "/deploy/version/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoBuildVO.Build>> queryLeoDeployVersion(@RequestBody @Valid LeoDeployParam.QueryDeployVersion queryBuildVersion) {
        return new HttpResult<>(deployFacade.queryLeoDeployVersion(queryBuildVersion));
    }

    @Operation(summary = "查询部署无状态")
    @PostMapping(value = "/deploy/deployment/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ApplicationResourceVO.BaseResource>> queryLeoDeployDeployment(@RequestBody @Valid LeoDeployParam.QueryDeployDeployment queryDeployDeployment) {
        return new HttpResult<>(deployFacade.queryLeoBuildDeployment(queryDeployDeployment));
    }

    @Operation(summary = "克隆部署无状态")
    @PostMapping(value = "/deploy/deployment/clone", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<DatasourceInstanceAsset>> cloneLeoDeployDeployment(@RequestBody @Valid LeoDeployParam.CloneDeployDeployment cloneDeployDeployment) {
        return new HttpResult<>(deployFacade.cloneDeployDeployment(cloneDeployDeployment));
    }

    @Operation(summary = "更新部署无状态(DevOps组需求)")
    @PutMapping(value = "/deploy/deployment/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoDeployDeployment(@RequestBody @Valid LeoDeployParam.UpdateDeployDeployment updateDeployDeployment) {
        deployFacade.updateDeployDeployment(updateDeployDeployment);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询部署无状态容器Env(DevOps组需求)")
    @PostMapping(value = "/deploy/deployment/container/env/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<EnvVar>> queryLeoDeployDeploymentContainerEnv(@RequestBody @Valid LeoDeployParam.QueryDeployDeploymentContainer queryDeployDeploymentContainer) {
        return new HttpResult<>(deployFacade.queryLeoDeployDeploymentContainerEnv(queryDeployDeploymentContainer));
    }

    @Operation(summary = "更新部署无状态容器Env(DevOps组需求)")
    @PutMapping(value = "/deploy/deployment/container/env/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoDeployDeploymentContainerEnv(@RequestBody @Valid LeoDeployParam.UpdateDeployDeploymentContainerEnv updateDeployDeploymentContainerEnv) {
        deployFacade.updateLeoDeployDeploymentContainerEnv(updateDeployDeploymentContainerEnv);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除部署无状态")
    @DeleteMapping(value = "/deploy/deployment/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delLeoDeployDeployment(@RequestParam @Schema(description = "资产ID") int assetId) {
        deployFacade.delDeployDeployment(assetId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "关闭部署")
    @PutMapping(value = "/deploy/close", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> closeLeoDeploy(@RequestParam @Valid int deployId) {
        deployFacade.closeDeploy(deployId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "停止部署(逻辑层)")
    @PutMapping(value = "/deploy/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> stopLeoDeploy(@RequestParam @Valid int deployId) {
        deployFacade.stopDeploy(deployId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询部署详情")
    @GetMapping(value = "/deploy/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeoDeployVO.Deploy> getLeoDeploy(@RequestParam @Valid int deployId) {
        return new HttpResult<>(deployFacade.getLeoDeploy(deployId));
    }

    // Pipeline

    @Operation(summary = "查询流水线节点步骤")
    @PostMapping(value = "/pipeline/node/steps/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoBuildPipelineVO.Step>> getPipelineRunNodeSteps(@RequestBody @Valid LeoBuildPipelineParam.GetPipelineRunNodeSteps param) {
        return new HttpResult<>(buildPipelineFacade.getPipelineRunNodeSteps(param));
    }

    // Rule

    @Operation(summary = "分页查询规则配置列表")
    @PostMapping(value = "/rule/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoRuleVO.Rule>> queryLeoRulePage(@RequestBody @Valid LeoRuleParam.RulePageQuery pageQuery) {
        return new HttpResult<>(ruleFacade.queryLeoRulePage(pageQuery));
    }

    @Operation(summary = "更新规则配置")
    @PutMapping(value = "/rule/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateLeoRule(@RequestBody @Valid LeoRuleParam.UpdateRule updateRule) {
        ruleFacade.updateLeoRule(updateRule);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "新增规则配置")
    @PostMapping(value = "/rule/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoRule(@RequestBody @Valid LeoRuleParam.AddRule addRule) {
        ruleFacade.addLeoRule(addRule);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的规则配置")
    @DeleteMapping(value = "/rule/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteLeoRuleById(@RequestParam @Valid int ruleId) {
        ruleFacade.deleteLeoRuleById(ruleId);
        return HttpResult.SUCCESS;
    }

    // Monitor

    @Operation(summary = "查询最近的构建详情")
    @PostMapping(value = "/latest/build/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoBuildVO.Build>> getLatestLeoBuild(@RequestBody @Valid LeoMonitorParam.QueryLatestBuild queryLatestBuild) {
        return new HttpResult<>(buildFacade.getLatestLeoBuild(queryLatestBuild));
    }

    @Operation(summary = "查询最近的部署详情")
    @PostMapping(value = "/latest/deploy/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<LeoDeployVO.Deploy>> getLatestLeoDeploy(@RequestBody @Valid LeoMonitorParam.QueryLatestDeploy queryLatestDeploy) {
        return new HttpResult<>(deployFacade.getLatestLeoDeploy(queryLatestDeploy));
    }

}