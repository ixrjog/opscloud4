package com.baiyi.opscloud.leo.handler.build.chain.pre;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsJobDriver;
import com.baiyi.opscloud.datasource.jenkins.model.JobWithDetails;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/21 15:57
 * @Version 1.0
 */
@Slf4j
@Component
public class DeleteHistoricalBuildJobChainHandler extends BaseBuildChainHandler {

    @Resource
    private LeoJobService jobService;

    @Resource
    private LeoBuildService buildService;

    @Resource
    private JenkinsJobDriver jenkinsJobDriver;

    /**
     * 保留数量
     */
    private static final int RESERVED_SIZE = 0;

    /**
     * 构建前删除历史构建任务
     *
     * @param leoBuild
     * @param buildConfig
     */
    @SuppressWarnings("ConstantValue")
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        LeoJob leoJob = jobService.getById(leoBuild.getJobId());
        List<LeoBuild> leoBuilds = buildService.queryTheHistoricalBuildToBeDeleted(leoJob.getId());
        // 少于保留数量则退出
        if (CollectionUtils.isEmpty(leoBuilds) || leoBuilds.size() <= RESERVED_SIZE) {
            return;
        }
        List<LeoBuild> subLeoBuilds = leoBuilds.subList(RESERVED_SIZE, leoBuilds.size());
        subLeoBuilds.forEach(build -> {
            try {
                deleteJob(build);
            } catch (Exception e) {
                log.warn("清理任务错误: buildId={}, {}", build.getId(), e.getMessage());
            }
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(build.getId())
                    .isDeletedBuildJob(true)
                    .build();
            buildService.updateByPrimaryKeySelective(saveLeoBuild);
        });
    }

    private void deleteJob(LeoBuild leoBuild) {
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(leoBuild.getBuildConfig());
        String uuid = Optional.ofNullable(buildConfig.getBuild())
                .map(LeoBuildModel.Build::getJenkins)
                .map(LeoBaseModel.Jenkins::getInstance)
                .map(LeoBaseModel.DsInstance::getUuid)
                .orElseThrow(() -> new LeoBuildException("无Jenkins实例属性，不需要删除任务!"));
        JenkinsConfig jenkinsConfig = getJenkinsConfigWithUuid(uuid);
        String jobName = leoBuild.getBuildJobName();
        deleteJob(leoBuild, jenkinsConfig, uuid, jobName);
    }

    private void deleteJob(LeoBuild leoBuild, JenkinsConfig jenkinsConfig, String uuid, String jobName) {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkinsConfig.getJenkins())) {
            JobWithDetails jobWithDetails = jenkinsServer.getJob(jobName);
            if (jobWithDetails != null) {
                jenkinsJobDriver.deleteJob(jenkinsConfig.getJenkins(), jobName);
                leoLog.info(leoBuild, "删除历史构建任务成功: instanceUuid={}, jobName={}", uuid, jobName);
            }
        } catch (URISyntaxException | IOException e) {
            leoLog.warn(leoBuild, "删除历史构建任务失败: instanceUuid={}, jobName={}", uuid, jobName);
        }
    }

}