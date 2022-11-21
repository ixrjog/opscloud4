package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsJobDriver;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.facade.leo.LeoBuildFacade;
import com.baiyi.opscloud.leo.build.LeoBuildHandler;
import com.baiyi.opscloud.leo.constants.ExecutionTypeConstants;
import com.baiyi.opscloud.leo.delegate.GitLabRepoDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.log.BuildingLogHelper;
import com.baiyi.opscloud.leo.util.JobUtil;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.Commit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    private final LeoJobService leoJobService;

    private final LeoBuildService leoBuildService;

    private final ApplicationService applicationService;

    private final JenkinsJobDriver jenkinsJobDriver;

    private final GitLabRepoDelegate gitLabRepoDelegate;

    private final ApplicationResourceService applicationResourceService;

    private final DsInstanceAssetService assetService;

    private final DsConfigHelper dsConfigHelper;

    private final BuildingLogHelper logHelper;

    private final LeoBuildHandler leoBuildHandler;


    @Override
    public void doBuild(LeoBuildParam.DoBuild doBuild) {
        LeoJob leoJob = leoJobService.getById(doBuild.getJobId());
        if (leoJob == null)
            throw new LeoBuildException("任务不存在: jobId={}", doBuild.getJobId());

        Application application = applicationService.getById(leoJob.getApplicationId());
        if (application == null)
            throw new LeoBuildException("应用不存在: jobId={}, applicationId={}", doBuild.getJobId(), leoJob.getApplicationId());

        final LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob.getJobConfig());

        List<String> tags = ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getTags)
                .orElseThrow(() -> new LeoBuildException("任务标签配置不存在: jobId={}", doBuild.getJobId()));

        LeoBaseModel.GitLab gitLab = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getGitLab)
                .orElseThrow(() -> new LeoBuildException("任务GitLab配置不存在: jobId={}", doBuild.getJobId()));

        LeoBaseModel.Notify notify = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getNotify)
                .orElse(LeoBaseModel.Notify.EMPTY_NOTIFY);


        List<LeoBaseModel.Parameter> jobParameters = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getParameters)
                .orElse(Lists.newArrayList());

        // 设置commitId
        DatasourceInstanceAsset gitLabProjectAsset = getGitLabProjectAssetWithLeoJobAndSshUrl(leoJob, gitLab.getProject().getSshUrl());
        final Long projectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(gitLabProjectAsset.getInstanceUuid());
        GitLabConfig gitLabConfig = dsConfigHelper.build(dsConfig, GitLabConfig.class);
        Commit commit = gitLabRepoDelegate.getBranchOrTagCommit(gitLabConfig.getGitlab(), projectId, doBuild.getBranch());
        gitLab.getProject().setCommit(
                LeoBaseModel.GitLabCommit.builder()
                        .id(commit.getId())
                        .build()
        );

        LeoBuildModel.Build build = LeoBuildModel.Build.builder()
                .tags(tags)
                .gitLab(gitLab)
                .notify(notify)
                .parameters(jobParameters)
                .build();

        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.BuildConfig.builder()
                .build(build)
                .build();

        final int buildNumber = generateBuildNumberWithJobId(doBuild.getJobId());
        final String jobName = JobUtil.generateJobName(application, leoJob);
        final String buildJobName = JobUtil.generateBuildJobName(application, leoJob, buildNumber);

        LeoBuild leoBuild = LeoBuild.builder()
                .jobId(doBuild.getJobId())
                .jobName(jobName)
                .buildJobName(buildJobName)
                .applicationId(leoJob.getApplicationId())
                .buildNumber(buildNumber)
                .versionName(JobUtil.generateVersionName(doBuild, jobConfig))
                .versionDesc(doBuild.getVersionDesc())
                .isFinish(false)
                .isDeletedBuildJob(false)
                .executionType(ExecutionTypeConstants.USER)
                .username(SessionUtil.getUsername())
                .buildConfig(buildConfig.dump())
                .build();
        leoBuildService.add(leoBuild);
        handleBuild(leoBuild, buildConfig);
    }

    /**
     * 使用责任链设计模式解耦代码
     *
     * @param leoBuild
     * @param buildConfig
     */
    private void handleBuild(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        leoBuildHandler.handleBuild(leoBuild, buildConfig);
    }

    /**
     * 生成构建编号
     * 当前最大值 + 1
     *
     * @param jobId
     * @return
     */
    private int generateBuildNumberWithJobId(int jobId) {
        return leoBuildService.getMaxBuildNumberWithJobId(jobId) + 1;
    }

    /**
     * 用户查询构建分支选项
     *
     * @param getOptions
     * @return
     */
    @Override
    public LeoBuildVO.BranchOptions getBuildBranchOptions(LeoBuildParam.GetBuildBranchOptions getOptions) {
        LeoJob leoJob = leoJobService.getById(getOptions.getJobId());
        if (leoJob == null) {
            throw new LeoBuildException("任务配置不存在: jobId={}", getOptions.getJobId());
        }

        DatasourceInstanceAsset gitLabProjectAsset = getGitLabProjectAssetWithLeoJobAndSshUrl(leoJob, getOptions.getSshUrl());
        final Long projectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        final boolean openTag = getOptions.getOpenTag() != null && getOptions.getOpenTag();

        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(gitLabProjectAsset.getInstanceUuid());
        GitLabConfig gitLabConfig = dsConfigHelper.build(dsConfig, GitLabConfig.class);

        return gitLabRepoDelegate.generatorGitLabBranchOptions(gitLabConfig.getGitlab(), projectId, openTag);
    }

    /**
     * 获取 GitLab Project Asset
     *
     * @param leoJob
     * @param sshUrl
     * @return
     */
    private DatasourceInstanceAsset getGitLabProjectAssetWithLeoJobAndSshUrl(LeoJob leoJob, String sshUrl) {
        ApplicationResource resource = applicationResourceService.queryByApplication(
                        leoJob.getApplicationId(),
                        DsAssetTypeConstants.GITLAB_PROJECT.name(),
                        BusinessTypeEnum.ASSET.getType())
                .stream()
                .filter(e -> e.getName().equals(sshUrl))
                .findFirst()
                // 未找到资产抛出异常
                .orElseThrow(() -> new LeoBuildException("GitLab项目不存在: applicationId={}, sshUrl={}", leoJob.getApplicationId(), sshUrl));
        return assetService.getById(resource.getBusinessId());
    }

}
