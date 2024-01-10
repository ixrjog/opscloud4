package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.annotation.SetSessionUsername;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.common.instance.OcInstance;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabRepositoryDriver;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.leo.LeoMonitorParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoBuildRequestParam;
import com.baiyi.opscloud.domain.util.GitFlowUtil;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.facade.leo.LeoBuildFacade;
import com.baiyi.opscloud.leo.aop.annotation.LeoBuildInterceptor;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.constants.BuildTypeConstants;
import com.baiyi.opscloud.leo.constants.ExecutionTypeConstants;
import com.baiyi.opscloud.leo.constants.HeartbeatTypeConstants;
import com.baiyi.opscloud.leo.delegate.GitLabRepoDelegate;
import com.baiyi.opscloud.leo.dict.IBuildDictGenerator;
import com.baiyi.opscloud.leo.dict.factory.BuildDictGeneratorFactory;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.driver.BlueRestDriver;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.LeoBuildHandler;
import com.baiyi.opscloud.leo.holder.LeoHeartbeatHolder;
import com.baiyi.opscloud.leo.log.LeoBuildingLog;
import com.baiyi.opscloud.leo.message.handler.impl.build.SubscribeLeoBuildRequestHandler;
import com.baiyi.opscloud.leo.packer.LeoBuildResponsePacker;
import com.baiyi.opscloud.leo.parser.MavenPublishParser;
import com.baiyi.opscloud.leo.util.JobUtil;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.RepositoryFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:15
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeoBuildFacadeImpl implements LeoBuildFacade {

    private final LeoJobService jobService;

    private final LeoBuildService buildService;

    private final LeoBuildImageService buildImageService;

    private final LeoDeployService deployService;

    private final ApplicationService applicationService;

    private final GitLabRepoDelegate gitLabRepoDelegate;

    private final ApplicationResourceService applicationResourceService;

    private final DsInstanceAssetService assetService;

    private final DsConfigManager dsConfigManager;

    private final LeoBuildingLog leoLog;

    private final LeoBuildHandler buildHandler;

    private final EnvService envService;

    private final BlueRestDriver blueRestDriver;

    private final LeoBuildResponsePacker leoBuildResponsePacker;

    private final LabelingMachineHelper labelingMachineHelper;

    private final SubscribeLeoBuildRequestHandler subscribeLeoBuildRequestHandler;

    private final DsInstanceAssetRelationService dsInstanceAssetRelationService;

    private final LeoHeartbeatHolder leoHeartbeatHolder;

    @Override
    @LeoBuildInterceptor(jobIdSpEL = "#doBuild.jobId")
    public LeoBuild doBuild(LeoBuildParam.DoBuild doBuild) {
        LeoJob leoJob = jobService.getById(doBuild.getJobId());
        if (leoJob == null) {
            throw new LeoBuildException("Leo job does not exist: jobId={}", doBuild.getJobId());
        }

        Application application = applicationService.getById(leoJob.getApplicationId());
        if (application == null) {
            throw new LeoBuildException("The application does not exist: jobId={}, applicationId={}", doBuild.getJobId(), leoJob.getApplicationId());
        }

        final LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob.getJobConfig());
        // 校验标签
        List<String> tags = ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getTags)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->tags", doBuild.getJobId()));
        // 校验gitLab
        LeoBaseModel.GitLab gitLab = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getGitLab)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->gitLab", doBuild.getJobId()));
        // 可选nexus
        LeoBaseModel.Nexus nexus = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getNexus)
                .orElse(LeoBaseModel.Nexus.EMPTY);
        // 通知配置
        LeoBaseModel.Notify notify = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getBuild)
                .map(LeoJobModel.Build::getNotify)
                .orElse(LeoBaseModel.Notify.EMPTY_NOTIFY);
        // 参数
        List<LeoBaseModel.Parameter> jobParameters = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getParameters)
                .orElse(Lists.newArrayList());

        // gitLab commit信息
        DatasourceInstanceAsset gitLabProjectAsset = getGitLabProjectAssetWithLeoJobAndSshUrl(leoJob, gitLab.getProject().getSshUrl());
        final Long projectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(gitLabProjectAsset.getInstanceUuid());
        GitLabConfig gitLabConfig = dsConfigManager.build(dsConfig, GitLabConfig.class);
        Commit commit = gitLabRepoDelegate.getBranchOrTagCommit(gitLabConfig.getGitlab(), projectId, doBuild.getBranch());
        gitLab.getProject().setCommit(
                LeoBaseModel.GitLabCommit.builder()
                        .id(commit.getId())
                        .message(commit.getMessage())
                        .webUrl(commit.getWebUrl())
                        .build()
        );

        // 构建类型
        final String buildType = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getBuild)
                .map(LeoJobModel.Build::getType)
                .orElse(BuildTypeConstants.KUBERNETES_IMAGE);

        // 生成字典
        Map<String, String> dict = generateDict(doBuild, buildType);

        // AutoDeploy
        LeoBaseModel.AutoDeploy autoDeploy;
        if (doBuild.getAutoDeploy()) {
            if (IdUtil.isEmpty(doBuild.getAssetId())) {
                throw new LeoBuildException("Enable automatic deployment after building without specifying asset ID parameters！");
            }
            autoDeploy = LeoBaseModel.AutoDeploy.builder()
                    .assetId(doBuild.getAssetId())
                    .jobId(leoJob.getId())
                    .build();
        } else {
            autoDeploy = LeoBaseModel.AutoDeploy.EMPTY;
        }

        LeoBuildModel.Build build = LeoBuildModel.Build.builder()
                .type(buildType)
                .dict(dict)
                .tags(tags)
                .gitLab(gitLab)
                .nexus(nexus)
                .autoDeploy(autoDeploy)
                .notify(notify)
                .parameters(jobParameters)
                .build();

        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.BuildConfig.builder()
                .build(build)
                .build();

        final int buildNumber = generateBuildNumberWithJobId(doBuild.getJobId());
        final String jobName = JobUtil.generateJobName(application, leoJob);
        final String buildJobName = JobUtil.generateBuildJobName(application, leoJob, buildNumber);

        Env env = envService.getByEnvType(leoJob.getEnvType());

        LeoBuild leoBuild = LeoBuild.builder()
                .jobId(doBuild.getJobId())
                .jobName(jobName)
                .buildJobName(buildJobName)
                .applicationId(leoJob.getApplicationId())
                .buildNumber(buildNumber)
                .versionName(JobUtil.generateVersionName(doBuild, jobConfig, gitLab, application, env, buildNumber))
                .versionDesc(doBuild.getVersionDesc())
                .isFinish(false)
                .isDeletedBuildJob(false)
                .isActive(false)
                .ticketId(0)
                .executionType(ExecutionTypeConstants.USER)
                .username(SessionHolder.getUsername())
                .buildConfig(buildConfig.dump())
                .ocInstance(OcInstance.OC_INSTANCE)
                .projectId(doBuild.getProjectId() == null ? 0 : doBuild.getProjectId())
                .build();
        buildService.add(leoBuild);
        // 打标签
        labelingMachineHelper.labeling(doBuild, BusinessTypeEnum.LEO_BUILD.getType(), leoBuild.getId());
        handleBuild(leoBuild, buildConfig);
        return leoBuild;
    }

    @Override
    @SetSessionUsername(usernameSpEL = "#doBuild.username")
    @LeoBuildInterceptor(jobIdSpEL = "#doBuild.jobId")
    public void doAutoBuild(LeoBuildParam.DoAutoBuild doBuild) {
        this.doBuild(doBuild.toDoBuild());
    }

    private Map<String, String> generateDict(LeoBuildParam.DoBuild doBuild, String buildType) {
        IBuildDictGenerator buildDictGenerator = BuildDictGeneratorFactory.getGenerator(buildType);
        if (buildDictGenerator == null) {
            throw new LeoBuildException("构建类型不正确: buildType={}", buildType);
        }
        return buildDictGenerator.generate(doBuild);
    }

    @Override
    public void closeBuild(int buildId) {
        LeoBuild leoBuild = buildService.getById(buildId);
        if (leoBuild == null) {
            throw new LeoBuildException("构建不存在: buildId={}", buildId);
        }
        if (leoBuild.getIsFinish() != null && leoBuild.getIsFinish()) {
            throw new LeoBuildException("构建任务已完成: buildId={}", buildId);
        }
        if (leoHeartbeatHolder.isLive(HeartbeatTypeConstants.BUILD, buildId)) {
            throw new LeoBuildException("构建任务有心跳: buildId={}", buildId);
        }
        LeoBuild saveLeoBuild = LeoBuild.builder()
                .id(buildId)
                .buildResult("ERROR")
                .buildStatus(StringFormatter.format("{} 关闭任务", SessionHolder.getUsername()))
                .isActive(false)
                .isFinish(true)
                .endTime(new Date())
                .build();
        buildService.updateByPrimaryKeySelective(saveLeoBuild);
    }

    @Override
    public void stopBuild(int buildId) {
        LeoBuild leoBuild = buildService.getById(buildId);
        if (leoBuild == null) {
            throw new LeoBuildException("构建不存在: buildId={}", buildId);
        }
        if (leoBuild.getIsFinish()) {
            throw new LeoBuildException("构建任务已经结束！");
        }
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(leoBuild);
        // step1 : stop jenkins 任务
        try {
            stopJenkinsPipeline(leoBuild, buildConfig);
        } catch (Exception e) {
            leoLog.warn(leoBuild, "Stop jenkins job err: {}", e.getMessage());
        }
        // step2 : stop leo build
        stopLeoBuild(leoBuild);
    }

    private void stopLeoBuild(LeoBuild leoBuild) {
        leoHeartbeatHolder.setStopSignal(HeartbeatTypeConstants.BUILD, leoBuild.getId());
    }

    private void stopJenkinsPipeline(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) throws LeoBuildException {
        final String jenkinsUuid = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getJenkins)
                .map(LeoBaseModel.Jenkins::getInstance)
                .map(LeoBaseModel.DsInstance::getUuid)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: build->jenkins->instance->uuid"));
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(jenkinsUuid);
        JenkinsConfig jenkinsConfig = dsConfigManager.build(dsConfig, JenkinsConfig.class);
        try {
            JenkinsPipeline.Step step = blueRestDriver.stopPipeline(jenkinsConfig.getJenkins(), leoBuild.getBuildJobName(), String.valueOf(1));
            leoLog.info(leoBuild, "Stop jenkins job: {}", JSONUtil.writeValueAsString(step));
        } catch (Exception e) {
            throw new LeoBuildException(e.getMessage());
        }
    }

    /**
     * 使用责任链设计模式解耦代码
     *
     * @param leoBuild
     * @param buildConfig
     */
    private void handleBuild(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        buildHandler.handleBuild(leoBuild, buildConfig);
    }

    /**
     * 生成构建编号
     * 当前最大值 + 1
     *
     * @param jobId
     * @return
     */
    private int generateBuildNumberWithJobId(int jobId) {
        return buildService.getMaxBuildNumberWithJobId(jobId) + 1;
    }

    /**
     * 用户查询构建分支选项
     *
     * @param options
     * @return
     */
    @Override
    public LeoBuildVO.BranchOptions getBuildBranchOptions(LeoBuildParam.GetBuildBranchOptions options) {
        LeoJob leoJob = jobService.getById(options.getJobId());
        if (leoJob == null) {
            throw new LeoBuildException("任务配置不存在: jobId={}", options.getJobId());
        }

        DatasourceInstanceAsset gitLabProjectAsset = getGitLabProjectAssetWithLeoJobAndSshUrl(leoJob, options.getSshUrl());
        final Long projectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        final boolean openTag = options.getOpenTag() != null && options.getOpenTag();
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(gitLabProjectAsset.getInstanceUuid());
        GitLabConfig gitLabConfig = dsConfigManager.build(dsConfig, GitLabConfig.class);
        LeoBuildVO.BranchOptions branchOptions = gitLabRepoDelegate.generatorGitLabBranchOptions(gitLabConfig.getGitlab(), projectId, openTag);
        // gitFlow 分支管控
        final boolean gitFlowEnabled = getGitFlowEnabled(leoJob, gitLabConfig);
        return filterGitFlow(branchOptions, leoJob, gitLabConfig, gitFlowEnabled);
    }

    @Override
    public LeoBuildVO.CompareResults compareBranch(LeoBuildParam.CompareBranch compareBranch) {
        LeoJob leoJob = jobService.getById(compareBranch.getJobId());
        if (leoJob == null) {
            throw new LeoBuildException("任务配置不存在: jobId={}", compareBranch.getJobId());
        }

        DatasourceInstanceAsset gitLabProjectAsset = getGitLabProjectAssetWithLeoJobAndSshUrl(leoJob, compareBranch.getSshUrl());
        final Long projectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(gitLabProjectAsset.getInstanceUuid());
        GitLabConfig gitLabConfig = dsConfigManager.build(dsConfig, GitLabConfig.class);

        final String to = StringUtils.isBlank(compareBranch.getTo()) ? "master" : compareBranch.getTo();
        return gitLabRepoDelegate.compareBranch(gitLabConfig.getGitlab(), projectId, compareBranch.getFrom(), to);
    }

    private boolean getGitFlowEnabled(LeoJob leoJob, GitLabConfig gitLabConfig) {
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);
        // 判断是否启用gitFlow
        boolean gitFlowEnabled = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getGitLab)
                .map(LeoBaseModel.GitLab::getGitFlow)
                .map(LeoBaseModel.GitFlow::getEnabled)
                .orElse(true);
        if (gitFlowEnabled) {
            gitFlowEnabled = Optional.of(gitLabConfig.getGitlab())
                    .map(GitLabConfig.GitLab::getGitFlow)
                    .map(GitLabConfig.GitFlow::getEnabled)
                    .orElse(false);
        }
        return gitFlowEnabled;
    }

    private LeoBuildVO.BranchOptions filterGitFlow(LeoBuildVO.BranchOptions branchOptions, LeoJob leoJob, GitLabConfig gitLabConfig, boolean gitFlowEnabled) {
        if (!gitFlowEnabled) {
            return branchOptions;
        }
        Map<String, List<String>> filter = Optional.of(gitLabConfig.getGitlab())
                .map(GitLabConfig.GitLab::getGitFlow)
                .map(GitLabConfig.GitFlow::getFilter)
                .orElseThrow(() -> new LeoBuildException("GitLab instance configuration enables gitFlow but does not configure filter filtering conditions"));
        Env env = envService.getByEnvType(leoJob.getEnvType());
        List<String> envFilter;
        if (filter.containsKey(env.getEnvName())) {
            envFilter = filter.get(env.getEnvName());
        } else {
            if (filter.containsKey("default")) {
                envFilter = filter.get("default");
            } else {
                envFilter = Lists.newArrayList("*");
            }
        }
        return GitFlowUtil.filter(branchOptions, envFilter);
    }

    @Override
    public LeoBuildVO.MavenPublishInfo getBuildMavenPublishInfo(LeoBuildParam.GetBuildMavenPublishInfo getBuildMavenPublishInfo) {
        LeoJob leoJob = jobService.getById(getBuildMavenPublishInfo.getJobId());
        if (leoJob == null) {
            throw new LeoBuildException("Configuration does not exist: jobId={}", getBuildMavenPublishInfo.getJobId());
        }
        DatasourceInstanceAsset gitLabProjectAsset = getGitLabProjectAssetWithLeoJobAndSshUrl(leoJob, getBuildMavenPublishInfo.getSshUrl());
        final Long projectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(gitLabProjectAsset.getInstanceUuid());
        GitLabConfig gitLabConfig = dsConfigManager.build(dsConfig, GitLabConfig.class);

        final String filePath = Optional.of(getBuildMavenPublishInfo)
                .map(LeoBuildParam.GetBuildMavenPublishInfo::getTools)
                .map(LeoBuildParam.BuildTools::getVersion)
                .map(LeoBuildParam.BuildToolsVersion::getFile)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: build->tools->version->file"));
        try {
            RepositoryFile repositoryFile = GitLabRepositoryDriver.getRepositoryFile(
                    gitLabConfig.getGitlab(),
                    projectId,
                    filePath,
                    getBuildMavenPublishInfo.getRef());
            final String content = new String(Base64.getDecoder().decode(repositoryFile.getContent()));
            LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);
            final LeoBaseModel.Nexus nexus = Optional.ofNullable(jobConfig)
                    .map(LeoJobModel.JobConfig::getJob)
                    .map(LeoJobModel.Job::getNexus)
                    .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->nexus"));
            return MavenPublishParser.parse(getBuildMavenPublishInfo.getTools(), nexus, content);
        } catch (GitLabApiException ignored) {
        }
        return LeoBuildVO.MavenPublishInfo.EMPTY_INFO;
    }

    @Override
    public LeoBuildVO.BranchOptions createBuildBranch(LeoBuildParam.CreateBuildBranch createBuildBranch) {
        LeoJob leoJob = jobService.getById(createBuildBranch.getJobId());
        if (leoJob == null) {
            throw new LeoBuildException("Leo job configuration does not exist: jobId={}", createBuildBranch.getJobId());
        }
        DatasourceInstanceAsset gitLabProjectAsset = getGitLabProjectAssetWithLeoJobAndSshUrl(leoJob, createBuildBranch.getSshUrl());
        final Long projectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(gitLabProjectAsset.getInstanceUuid());
        GitLabConfig gitLabConfig = dsConfigManager.build(dsConfig, GitLabConfig.class);
        return gitLabRepoDelegate.createGitLabBranch(gitLabConfig.getGitlab(), projectId, createBuildBranch.getRef());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLeoBuild(LeoBuildParam.UpdateBuild updateBuild) {
        LeoBuild leoBuild = buildService.getById(updateBuild.getId());
        if (!leoBuild.getIsFinish()) {
            throw new LeoBuildException("Cannot modify build details before the build task is completed！");
        }

        final String versionDesc = StringUtils.isNotBlank(updateBuild.getVersionDesc()) ? updateBuild.getVersionDesc() : null;

        LeoBuild saveLeoBuild = LeoBuild.builder()
                .id(updateBuild.getId())
                .versionName(updateBuild.getVersionName())
                .versionDesc(versionDesc)
                .isActive(updateBuild.getIsActive())
                .build();
        buildService.updateByPrimaryKeySelective(saveLeoBuild);
        // 更新镜像版本信息
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(leoBuild.getBuildConfig());
        Map<String, String> dict = buildConfig.getBuild().getDict();
        LeoBuildImage leoBuildImage = buildImageService.getByUniqueKey(leoBuild.getId(), dict.get(BuildDictConstants.IMAGE.getKey()));
        if (leoBuildImage == null) {
            return;
        }
        LeoBuildImage saveLeoBuildImage = LeoBuildImage.builder()
                .id(leoBuildImage.getId())
                .versionName(updateBuild.getVersionName())
                .versionDesc(StringUtils.isNotBlank(updateBuild.getVersionDesc()) ? updateBuild.getVersionDesc() : null)
                .build();
        buildImageService.updateByPrimaryKeySelective(saveLeoBuildImage);
        // 更新部署记录
        List<LeoDeploy> deploys = deployService.queryWithBuildId(updateBuild.getId());
        if (!CollectionUtils.isEmpty(deploys)) {
            for (LeoDeploy deploy : deploys) {
                LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                        .id(deploy.getId())
                        .versionName(updateBuild.getVersionName())
                        .versionDesc(versionDesc)
                        .build();
                deployService.updateByPrimaryKeySelective(saveLeoDeploy);
            }
        }
    }

    /**
     * 获取 GitLab Project Asset
     *
     * @param leoJob
     * @param sshUrl
     * @return
     */
    private DatasourceInstanceAsset getGitLabProjectAssetWithLeoJobAndSshUrl(LeoJob leoJob, String sshUrl) {
        Optional<ApplicationResource> optionalResource = applicationResourceService.queryByApplication(
                        leoJob.getApplicationId(),
                        DsAssetTypeConstants.GITLAB_PROJECT.name(),
                        BusinessTypeEnum.ASSET.getType())
                .stream()
                .filter(e -> e.getName().equals(sshUrl))
                .findFirst();

        if (optionalResource.isPresent()) {
            return assetService.getById(optionalResource.get().getBusinessId());
        } else {
            return getGitLabProjectAssetWithGroup(leoJob.getApplicationId(), sshUrl);
        }
    }

    /**
     * 从 gitLab group 查找 project
     *
     * @param applicationId
     * @param sshUrl
     * @return
     */
    private DatasourceInstanceAsset getGitLabProjectAssetWithGroup(int applicationId, String sshUrl) {
        // 查询所有的 gitLab group
        List<ApplicationResource> resources = applicationResourceService.queryByApplication(
                applicationId,
                DsAssetTypeConstants.GITLAB_GROUP.name(),
                BusinessTypeEnum.ASSET.getType());
        if (!CollectionUtils.isEmpty(resources)) {
            for (ApplicationResource resource : resources) {
                List<DatasourceInstanceAssetRelation> assetRelations = dsInstanceAssetRelationService.queryTargetAsset(resource.getBusinessId());
                for (DatasourceInstanceAssetRelation assetRelation : assetRelations) {
                    DatasourceInstanceAsset asset = assetService.getById(assetRelation.getTargetAssetId());
                    if (sshUrl.equals(asset.getAssetKey())) {
                        return asset;
                    }
                }
            }
        }
        throw new LeoBuildException("GitLab project {} does not exist: applicationId={}", sshUrl, applicationId);
    }

    @Override
    public DataTable<LeoBuildVO.Build> queryLeoJobBuildPage(LeoJobParam.JobBuildPageQuery pageQuery) {
        DataTable<LeoBuild> table = buildService.queryBuildPage(pageQuery);
        List<LeoBuildVO.Build> data = BeanCopierUtil.copyListProperties(table.getData(), LeoBuildVO.Build.class).stream()
                .peek(leoBuildResponsePacker::wrap)
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public LeoBuildVO.Build getLeoBuild(int buildId) {
        LeoBuild leoBuild = buildService.getById(buildId);
        if (leoBuild == null) {
            throw new LeoBuildException("Build information does not exist: buildId={}", buildId);
        }
        LeoBuildVO.Build build = BeanCopierUtil.copyProperties(leoBuild, LeoBuildVO.Build.class);
        leoBuildResponsePacker.wrap(build);
        return build;
    }

    @Override
    public DataTable<LeoBuildVO.Build> queryMyLeoJobBuildPage(SubscribeLeoBuildRequestParam pageQuery) {
        return subscribeLeoBuildRequestHandler.queryLeoBuildPage(pageQuery);
    }

    @Override
    public List<LeoBuildVO.Build> getLatestLeoBuild(LeoMonitorParam.QueryLatestBuild queryLatestBuild) {
        List<LeoBuild> builds = buildService.queryLatestLeoBuild(queryLatestBuild);
        return BeanCopierUtil.copyListProperties(builds, LeoBuildVO.Build.class).stream()
                .peek(e -> {
                    leoBuildResponsePacker.wrap(e);
                    e.setIsLive(leoHeartbeatHolder.isLive(HeartbeatTypeConstants.BUILD, e.getId()));
                })
                .collect(Collectors.toList());
    }

}