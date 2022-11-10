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
import com.baiyi.opscloud.leo.constants.ExecutionTypeConstants;
import com.baiyi.opscloud.leo.delegate.GitLabRepoDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.util.JobNameUtil;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        LeoBuildModel.Build build = LeoBuildModel.Build.builder()
                .tags(tags)
                .gitLab(gitLab)
                .parameters(null)
                .comment(null)
                .build();

        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.BuildConfig.builder()
                .build(build)
                .build();

        final int buildNumber = generateBuildNumberWithJobId(doBuild.getJobId());

        LeoBuild leoBuild = LeoBuild.builder()
                .jobId(doBuild.getJobId())
                .jobName(JobNameUtil.generateBuildJobName(application, leoJob, buildNumber))
                .applicationId(leoJob.getApplicationId())
                .buildNumber(buildNumber)
                .versionName(doBuild.getVersionName())
                .versionDesc(doBuild.getVersionDesc())
                .executionType(ExecutionTypeConstants.USER)
                .username(SessionUtil.getUsername())
                .buildConfig(buildConfig.dump())
                .build();

        leoBuildService.add(leoBuild);
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

    @Override
    public LeoBuildVO.BranchOptions getBuildBranchOptions(LeoBuildParam.GetBuildBranchOptions getOptions) {
        LeoJob leoJob = leoJobService.getById(getOptions.getJobId());
        if (leoJob == null) {
            throw new LeoBuildException("任务配置不存在: jobId={}", getOptions.getJobId());
        }
        ApplicationResource resource = applicationResourceService.queryByApplication(
                        leoJob.getApplicationId(),
                        DsAssetTypeConstants.GITLAB_PROJECT.name(),
                        BusinessTypeEnum.ASSET.getType())
                .stream()
                .filter(e -> e.getName().equals(getOptions.getSshUrl()))
                .findFirst()
                .orElseThrow(() -> new LeoBuildException("GitLab项目不存在: applicationId={}, sshUrl={}", leoJob.getApplicationId(), getOptions.getSshUrl()));

        DatasourceInstanceAsset gitLabProjectAsset = assetService.getById(resource.getBusinessId());

        final Long projectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        final boolean openTag = getOptions.getOpenTag() != null && getOptions.getOpenTag();

        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(gitLabProjectAsset.getInstanceUuid());
        GitLabConfig gitLabConfig = dsConfigHelper.build(dsConfig, GitLabConfig.class);

        return gitLabRepoDelegate.generatorGitLabBranchOptions(gitLabConfig.getGitlab(), projectId, openTag);
    }

}
