package com.baiyi.opscloud.leo.action.build.concrete.pre;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsJobDriver;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.model.JobWithDetails;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.action.build.BaseBuildHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
public class DeleteHistoricalBuildJobConcreteHandler extends BaseBuildHandler {

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private LeoBuildService leoBuildService;

    @Resource
    private JenkinsJobDriver jenkinsJobDriver;

    @Resource
    private ApplicationService applicationService;

    // 保留数量
    private static final int RESERVED_SIZE = 1;

    /**
     * 构建前删除历史构建任务
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        LeoJob leoJob = leoJobService.getById(leoBuild.getJobId());
        List<LeoBuild> leoBuilds = leoBuildService.queryTheHistoricalBuildToBeDeleted(leoJob.getId());
        // 少于保留数量则退出
        if (CollectionUtils.isEmpty(leoBuilds) || leoBuilds.size() <= RESERVED_SIZE)
            return;
        List<LeoBuild> subLeoBuilds = leoBuilds.subList(RESERVED_SIZE, leoBuilds.size());

        subLeoBuilds.forEach(e -> {
            try {
                deleteJob(e);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(e.getId())
                    .isDeletedBuildJob(true)
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
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
        try {
            JobWithDetails jobWithDetails = JenkinsServerDriver.getJob(jenkinsConfig.getJenkins(), jobName);
            if (jobWithDetails != null) {
                jenkinsJobDriver.deleteJob(jenkinsConfig.getJenkins(), jobName);
            }
        } catch (URISyntaxException | IOException e) {
            logHelper.warn(leoBuild, "删除Jenkins历史构建任务失败: instanceUuid={}, jobName={}", uuid, jobName);
        }
        logHelper.info(leoBuild, "删除Jenkins历史构建任务成功: instanceUuid={}, jobName={}", uuid, jobName);
    }

}


