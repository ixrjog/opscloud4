package com.baiyi.opscloud.leo.handler.build.strategy.build.base;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsJobDriver;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.constants.BuildStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.base.BaseBuildStrategy;
import com.baiyi.opscloud.leo.handler.build.helper.ApplicationTagsHelper;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/4/26 11:33
 * @Version 1.0
 */
public abstract class BaseDoBuildStrategy extends BaseBuildStrategy {

    @Override
    public String getStep() {
        return BuildStepConstants.DO_BUILD.name();
    }

    @Resource
    private LeoJobService jobService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private EnvService envService;

    @Resource
    private JenkinsJobDriver jenkinsJobDriver;

    @Resource
    private UserService userService;

    @Resource
    private ApplicationTagsHelper applicationTagsHelper;

    @Resource
    private ApplicationResourceService applicationResourceService;

    /**
     * 执行构建
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        LeoBaseModel.DsInstance dsInstance = buildConfig.getBuild().getJenkins().getInstance();
        JenkinsConfig jenkinsConfig = getJenkinsConfigWithUuid(dsInstance.getUuid());
        try {
            LeoJob leoJob = jobService.getById(leoBuild.getJobId());
            // 先构建字典，再构建参数
            Map<String, String> dict = handleGenerateDict(leoBuild, buildConfig, leoJob);
            buildConfig.getBuild().setDict(dict);
            Map<String, String> params = generateBuildParamMap(leoBuild, buildConfig, leoJob);
            jenkinsJobDriver.buildJobWithParams(jenkinsConfig.getJenkins(), leoBuild.getBuildJobName(), params);
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("执行构建任务阶段: 成功")
                    .buildConfig(buildConfig.dump())
                    // 设置启动时间
                    .startTime(new Date())
                    .build();
            save(saveLeoBuild, "执行构建任务成功: jenkinsName={}, jobName={}", dsInstance.getName(), leoBuild.getBuildJobName());
        } catch (URISyntaxException | IOException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .endTime(new Date())
                    .isFinish(true)
                    .buildResult(RESULT_ERROR)
                    .buildStatus("执行构建任务阶段")
                    .build();
            save(saveLeoBuild);
            throw new LeoBuildException("执行构建任务错误: jenkinsName={}, uuid={}, jobName={}", dsInstance.getName(), dsInstance.getUuid(), leoBuild.getBuildJobName());
        }
    }

    /**
     * 生成构建参数
     *
     * @param leoBuild
     * @param buildConfig
     * @param leoJob
     * @return
     */
    private Map<String, String> generateBuildParamMap(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, LeoJob leoJob) {
        List<LeoBaseModel.Parameter> jobParameters = Optional.of(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getParameters)
                .orElse(Lists.newArrayList());

        Map<String, String> dict = Optional.of(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getDict)
                .orElseThrow(() -> new LeoBuildException("执行构建任务错误, 构建字典不存在！"));

        Map<String, String> paramMap = toParamMap(jobParameters);

        paramMap.put(BuildDictConstants.PROJECT.getKey(), dict.get(BuildDictConstants.PROJECT.getKey()));
        // kubernetes-image
        paramMap.put(BuildDictConstants.REGISTRY_URL.getKey(), dict.get(BuildDictConstants.REGISTRY_URL.getKey()));
        paramMap.put(BuildDictConstants.BRANCH.getKey(), dict.get(BuildDictConstants.BRANCH.getKey()));
        paramMap.put(BuildDictConstants.COMMIT_ID.getKey(), dict.get(BuildDictConstants.COMMIT_ID.getKey()));
        paramMap.put(BuildDictConstants.SSH_URL.getKey(), dict.get(BuildDictConstants.SSH_URL.getKey()));
        paramMap.put(BuildDictConstants.ENV.getKey(), dict.get(BuildDictConstants.ENV.getKey()));
        paramMap.put(BuildDictConstants.JOB_BUILD_NUMBER.getKey(), String.valueOf(leoBuild.getBuildNumber()));
        paramMap.put(BuildDictConstants.APPLICATION_NAME.getKey(), dict.get(BuildDictConstants.APPLICATION_NAME.getKey()));

        return paramMap;
    }

    private Map<String, String> handleGenerateDict(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, LeoJob leoJob) {
        Map<String, String> dict = generateBaseDict(leoBuild, buildConfig, leoJob);
        handlePostProcessDict(leoBuild, buildConfig, leoJob, dict);
        return dict;
    }

    /**
     * 后加工数据
     *
     * @param leoBuild
     * @param buildConfig
     * @param leoJob
     * @param dict
     */
    protected abstract void handlePostProcessDict(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, LeoJob leoJob, Map<String, String> dict);

    /**
     * 生成基础构建字典
     *
     * @param leoBuild
     * @param buildConfig
     * @return
     */
    private Map<String, String> generateBaseDict(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, LeoJob leoJob) {
        // 构建字典
        Map<String, String> dict = Optional.of(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getDict)
                .orElse(Maps.newHashMap());

        List<LeoBaseModel.Parameter> jobParameters = buildJobParameters(buildConfig);
        Map<String, String> paramMap;
        try {
            paramMap = jobParameters.stream()
                    .collect(Collectors.toMap(LeoBaseModel.Parameter::getName, LeoBaseModel.Parameter::getValue, (k1, k2) -> k1));
        } catch (NullPointerException e) {
            throw new LeoBuildException("任务参数配置不正确: job->parameters");
        }

        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);

        User user = userService.getByUsername(leoBuild.getUsername());
        final String displayName = Optional.ofNullable(user)
                .map(User::getDisplayName)
                .orElse(Optional.ofNullable(user)
                        .map(User::getName)
                        .orElse(leoBuild.getUsername())
                );

        final String applicationTags = applicationTagsHelper.getTagsStr(leoJob.getApplicationId());
        Application application = applicationService.getById(leoJob.getApplicationId());

        LeoBaseModel.GitLabProject gitLabProject = Optional.of(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getGitLab)
                .map(LeoBaseModel.GitLab::getProject)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: build->gitLab->project"));

        fillGitLabProjectSshUrl(application, gitLabProject);

        final String envName = getEnvName(leoJob);
        final String commit = gitLabProject.getCommit().getId();
        final String commitId = gitLabProject.getCommit().getId().substring(0, 8);
        final String buildNumber = String.valueOf(leoBuild.getBuildNumber());

        final String applicationName = application.getApplicationKey();

        dict.put(BuildDictConstants.ENV.getKey(), envName);
        dict.put(BuildDictConstants.APPLICATION_NAME.getKey(), applicationName);
        dict.put(BuildDictConstants.APPLICATION_TAGS.getKey(), applicationTags);
        dict.put(BuildDictConstants.JOB_NAME.getKey(), leoBuild.getJobName());
        dict.put(BuildDictConstants.VERSION_NAME.getKey(), leoBuild.getVersionName());
        dict.put(BuildDictConstants.BUILD_NUMBER.getKey(), buildNumber);
        dict.put(BuildDictConstants.COMMIT.getKey(), commit);
        dict.put(BuildDictConstants.COMMIT_ID.getKey(), commitId);
        dict.put(BuildDictConstants.SSH_URL.getKey(), gitLabProject.getSshUrl());
        dict.put(BuildDictConstants.DISPLAY_NAME.getKey(), displayName);

        String project;
        if (paramMap.containsKey(BuildDictConstants.PROJECT.getKey())) {
            project = paramMap.get(BuildDictConstants.PROJECT.getKey());
        } else {
            project = Optional.ofNullable(jobConfig)
                    .map(LeoJobModel.JobConfig::getJob)
                    .map(LeoJobModel.Job::getCr)
                    .map(LeoJobModel.CR::getRepo)
                    .map(LeoJobModel.Repo::getName)
                    .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->gitLab->project"));
        }
        dict.put(BuildDictConstants.PROJECT.getKey(), project);
        return dict;
    }

    private String getEnvName(final LeoJob leoJob) {
        return envService.getByEnvType(leoJob.getEnvType()).getEnvName();
    }

    private void fillGitLabProjectSshUrl(Application application, LeoBaseModel.GitLabProject gitLabProject) {
        if (StringUtils.isNotBlank(gitLabProject.getSshUrl())) {
            return;
        }
        List<ApplicationResource> resources = applicationResourceService.queryByApplication(application.getId(), DsAssetTypeConstants.GITLAB_PROJECT.name());
        if (CollectionUtils.isEmpty(resources) || resources.size() > 1) {
            throw new LeoBuildException("Configuration does not exist: gitLab->project->sshUrl");
        }
        gitLabProject.setSshUrl(resources.getFirst().getName());
    }

    /**
     * 配置文件中的构建参数
     *
     * @param buildConfig
     * @return
     */
    protected List<LeoBaseModel.Parameter> buildJobParameters(LeoBuildModel.BuildConfig buildConfig) {
        return Optional.of(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getParameters)
                .orElse(Lists.newArrayList());
    }

    /**
     * 构建参数转换Map
     *
     * @param jobParameters
     * @return
     */
    protected Map<String, String> toParamMap(List<LeoBaseModel.Parameter> jobParameters) {
        return jobParameters.stream()
                .collect(Collectors.toMap(LeoBaseModel.Parameter::getName, LeoBaseModel.Parameter::getValue, (k1, k2) -> k1));
    }

}