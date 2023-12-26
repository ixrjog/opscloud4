package com.baiyi.opscloud.leo.handler.build.chain.pre;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsJobDriver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/11/14 17:50
 * @Version 1.0
 */
@Slf4j
@Component
public class CreateJobChainHandler extends BaseBuildChainHandler {

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private JenkinsJobDriver jenkinsJobDriver;

    /**
     * 在Jenkins实例中创建任务
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        LeoBaseModel.DsInstance dsInstance = buildConfig.getBuild().getJenkins().getInstance();
        JenkinsConfig jenkinsConfig = getJenkinsConfigWithUuid(dsInstance.getUuid());
        LeoJob leoJob = leoJobService.getById(leoBuild.getJobId());
        if (leoJob == null) {
            throw new LeoBuildException("任务不存在: jobId={}", leoBuild.getJobId());
        }
        if (StringUtils.isBlank(leoJob.getTemplateContent())) {
            throw new LeoBuildException("任务模板内容不存在: jobId={}", leoBuild.getJobId());
        }
        try {
            jenkinsJobDriver.createJob(jenkinsConfig.getJenkins(), leoBuild.getBuildJobName(), leoJob.getTemplateContent());
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("创建构建任务阶段: 成功")
                    .build();
            save(saveLeoBuild, "创建构建任务成功: jenkinsName={}, jobName={}", dsInstance.getName(), leoBuild.getBuildJobName());
        } catch (URISyntaxException | IOException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .endTime(new Date())
                    .isFinish(true)
                    .buildResult(RESULT_ERROR)
                    .buildStatus("创建构建任务阶段")
                    .build();
            save(saveLeoBuild);
            throw new LeoBuildException("创建构建任务错误: jenkinsName={}, uuid={}", dsInstance.getName(), dsInstance.getUuid());
        }
    }

}