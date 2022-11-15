package com.baiyi.opscloud.leo.build.concrete;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsJobDriver;
import com.baiyi.opscloud.datasource.jenkins.model.BuildResult;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.build.BaseBuildHandler;
import com.baiyi.opscloud.leo.constants.BuildParameterConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
            jenkinsJobDriver.buildJobWithParams(jenkinsConfig.getJenkins(), leoBuild.getBuildJobName(), toBuildParamMap(leoBuild, buildConfig));
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("执行构建任务阶段: 成功")
                    // 设置启动时间
                    .startTime(new Date())
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
            logHelper.info(leoBuild, "执行构建任务成功: jenkinsName={}, jobName={}", dsInstance.getName(), leoBuild.getBuildJobName());
        } catch (URISyntaxException | IOException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .endTime(new Date())
                    .isFinish(true)
                    .buildResult(BuildResult.FAILURE.name())
                    .buildStatus("执行构建任务阶段")
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
            throw new LeoBuildException("执行构建任务错误: jenkinsName={}, uuid={}, jobName={}", dsInstance.getName(), dsInstance.getUuid(), leoBuild.getBuildJobName());
        }
    }

    private Map<String, String> toBuildParamMap(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
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

        LeoJob leoJob = leoJobService.getById(leoBuild.getJobId());

        paramMap.put(BuildParameterConstants.BRANCH.getParam(), gitLabProject.getBranch());
        paramMap.put(BuildParameterConstants.COMMIT_ID.getParam(), gitLabProject.getCommit().getId());
        paramMap.put(BuildParameterConstants.SSH_URL.getParam(), gitLabProject.getSshUrl());
        paramMap.put(BuildParameterConstants.ENV.getParam(), getEnvNameWithLeoJob(leoJob));
        paramMap.put(BuildParameterConstants.JOB_BUILD_NUMBER.getParam(), String.valueOf(leoBuild.getBuildNumber()));
        paramMap.put(BuildParameterConstants.APPLICATION_NAME.getParam(), applicationService.getById(leoJob.getApplicationId()).getApplicationKey());
        return paramMap;
    }

    private String getEnvNameWithLeoJob(LeoJob leoJob) {
        return envService.getByEnvType(leoJob.getEnvType()).getEnvName();
    }

}
