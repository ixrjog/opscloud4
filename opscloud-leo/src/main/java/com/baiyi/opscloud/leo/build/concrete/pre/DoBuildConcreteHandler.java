package com.baiyi.opscloud.leo.build.concrete.pre;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsJobDriver;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.leo.build.BaseBuildHandler;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.constants.BuildParameterConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/14 19:47
 * @Version 1.0
 */
@Slf4j
@Component
public class DoBuildConcreteHandler extends BaseBuildHandler {

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private EnvService envService;

    @Resource
    private JenkinsJobDriver jenkinsJobDriver;

    @Resource
    private UserService userService;

    @Resource
    private BusinessTagService bizTagService;

    @Resource
    private TagService tagService;


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
            LeoJob leoJob = leoJobService.getById(leoBuild.getJobId());
            Map<String, String> params = toBuildParamMap(leoBuild, buildConfig, leoJob);
            Map<String, String> dict = generateBuildDict(leoBuild, buildConfig, leoJob, params);
            buildConfig.getBuild().setDict(dict);

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
                    .buildResult(BUILD_RESULT_ERROR)
                    .buildStatus("执行构建任务阶段")
                    .build();
            save(saveLeoBuild);
            throw new LeoBuildException("执行构建任务错误: jenkinsName={}, uuid={}, jobName={}", dsInstance.getName(), dsInstance.getUuid(), leoBuild.getBuildJobName());
        }
    }

    private Map<String, String> toBuildParamMap(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, LeoJob leoJob) {
        List<LeoBaseModel.Parameter> jobParameters = Optional.of(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getParameters)
                .orElse(Lists.newArrayList());
        LeoBaseModel.GitLabProject gitLabProject = Optional.of(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getGitLab)
                .map(LeoBaseModel.GitLab::getProject)
                .orElseThrow(() -> new LeoBuildException("执行构建任务错误, 未指GitLab项目配置！"));

        Map<String, String> paramMap = jobParameters.stream()
                .collect(Collectors.toMap(LeoBaseModel.Parameter::getName, LeoBaseModel.Parameter::getValue, (k1, k2) -> k1));

        final String env = getEnvNameWithLeoJob(leoJob);
        final String applicationName = applicationService.getById(leoJob.getApplicationId()).getApplicationKey();

        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);
        if (!paramMap.containsKey(BuildParameterConstants.REGISTRY_URL.getParam())) {
            final String registryUrl = Optional.ofNullable(jobConfig)
                    .map(LeoJobModel.JobConfig::getJob)
                    .map(LeoJobModel.Job::getCr)
                    .map(LeoJobModel.CR::getInstance)
                    .map(LeoJobModel.CRInstance::getUrl)
                    .orElse("");
            if (StringUtils.isNotBlank(registryUrl))
                paramMap.put(BuildParameterConstants.REGISTRY_URL.getParam(), registryUrl);
        }
        // repoName -> parameters.project
        Optional<String> optionalRepoName = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getCr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getName);
        optionalRepoName.ifPresent(s -> paramMap.put(BuildParameterConstants.PROJECT.getParam(), s));

        paramMap.put(BuildParameterConstants.BRANCH.getParam(), gitLabProject.getBranch());
        paramMap.put(BuildParameterConstants.COMMIT_ID.getParam(), gitLabProject.getCommit().getId().substring(0, 8));
        paramMap.put(BuildParameterConstants.SSH_URL.getParam(), gitLabProject.getSshUrl());
        paramMap.put(BuildParameterConstants.ENV.getParam(), env);
        paramMap.put(BuildParameterConstants.JOB_BUILD_NUMBER.getParam(), String.valueOf(leoBuild.getBuildNumber()));
        paramMap.put(BuildParameterConstants.APPLICATION_NAME.getParam(), applicationName);

        return paramMap;
    }

    /**
     * 生成构建字典
     *
     * @param leoBuild
     * @param buildConfig
     * @param params
     * @return
     */
    private Map<String, String> generateBuildDict(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, LeoJob leoJob, Map<String, String> params) {
        // 构建字典
        Map<String, String> dict = Optional.of(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getDict)
                .orElse(Maps.newHashMap());

        User user = userService.getByUsername(leoBuild.getUsername());
        final String displayName = Optional.ofNullable(user)
                .map(User::getDisplayName)
                .orElse(Optional.ofNullable(user)
                        .map(User::getName)
                        .orElse(leoBuild.getUsername())
                );

        SimpleBusiness simpleBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .businessId(leoJob.getApplicationId())
                .build();

        List<BusinessTag> bizTags = bizTagService.queryByBusiness(simpleBusiness);
        final String applicationTags = CollectionUtils.isEmpty(bizTags) ? "未定义标签" : Joiner.on("、").join(
                bizTags.stream().map(t ->
                        tagService.getById(t.getTagId()).getTagKey()
                ).collect(Collectors.toList()));

        final String envName = params.get(BuildParameterConstants.ENV.getParam());
        final String shortCommit = params.get(BuildParameterConstants.COMMIT_ID.getParam());
        final String buildNumber = String.valueOf(leoBuild.getBuildNumber());
        dict.put(BuildDictConstants.ENV_NAME.getKey(), envName);
        dict.put(BuildDictConstants.APPLICATION_NAME.getKey(), params.get(BuildParameterConstants.APPLICATION_NAME.getParam()));
        dict.put(BuildDictConstants.APPLICATION_TAGS.getKey(), applicationTags);
        dict.put(BuildDictConstants.JOB_NAME.getKey(), leoBuild.getJobName());
        dict.put(BuildDictConstants.VERSION_NAME.getKey(), leoBuild.getVersionName());
        dict.put(BuildDictConstants.BUILD_NUMBER.getKey(), buildNumber);
        dict.put(BuildDictConstants.BRANCH.getKey(), params.get(BuildParameterConstants.BRANCH.getParam()));
        dict.put(BuildDictConstants.COMMIT.getKey(), shortCommit);
        dict.put(BuildDictConstants.DISPLAY_NAME.getKey(), displayName);

        String project = params.get(BuildParameterConstants.PROJECT.getParam());
        String registryUrl = params.get(BuildParameterConstants.REGISTRY_URL.getParam());
        // example: 460e7585-19
        String imageTag = Joiner.on("-").join(shortCommit, buildNumber);
        // example: aliyun-cr-uk.chuanyinet.com/daily/merchant-rss:460e7585-19
        dict.put(BuildDictConstants.IMAGE.getKey(), String.format("%s/%s/%s:%s", registryUrl, envName, project, imageTag));
        dict.put(BuildDictConstants.IMAGE_TAG.getKey(), imageTag);
        return dict;
    }

    private String getEnvNameWithLeoJob(final LeoJob leoJob) {
        return envService.getByEnvType(leoJob.getEnvType()).getEnvName();
    }

}