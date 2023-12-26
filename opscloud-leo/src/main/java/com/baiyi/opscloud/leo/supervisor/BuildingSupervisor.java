package com.baiyi.opscloud.leo.supervisor;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.model.Build;
import com.baiyi.opscloud.datasource.jenkins.model.BuildResult;
import com.baiyi.opscloud.datasource.jenkins.model.BuildWithDetails;
import com.baiyi.opscloud.datasource.jenkins.model.JobWithDetails;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.constants.HeartbeatTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.StopBuildFlag;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.LeoPostBuildHandler;
import com.baiyi.opscloud.leo.log.LeoBuildingLog;
import com.baiyi.opscloud.leo.holder.LeoHeartbeatHolder;
import com.baiyi.opscloud.leo.supervisor.base.ISupervisor;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import static com.baiyi.opscloud.common.util.NewTimeUtil.MINUTE_TIME;
import static com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler.RESULT_ERROR;

/**
 * 构建事件循环
 *
 * @Author baiyi
 * @Date 2022/11/9 11:43
 * @Version 1.0
 */
@Slf4j
public class BuildingSupervisor implements ISupervisor {

    private static final int SLEEP_SECONDS = 4;

    private static final long TIMEOUT = 60 * MINUTE_TIME;

    private final LeoBuildService leoBuildService;

    private final LeoBuildingLog leoLog;

    private final JenkinsConfig.Jenkins jenkins;

    private final LeoPostBuildHandler leoPostBuildHandler;

    private final LeoHeartbeatHolder heartbeatHolder;

    private final LeoBuildModel.BuildConfig buildConfig;

    private Build build;

    private final LeoBuild leoBuild;

    public BuildingSupervisor(LeoHeartbeatHolder heartbeatHolder,
                              LeoBuildService leoBuildService,
                              LeoBuild leoBuild,
                              LeoBuildingLog leoLog,
                              JenkinsConfig.Jenkins jenkins,
                              LeoBuildModel.BuildConfig buildConfig,
                              LeoPostBuildHandler leoPostBuildHandler) {
        this.heartbeatHolder = heartbeatHolder;
        this.leoBuildService = leoBuildService;
        this.leoBuild = leoBuild;
        this.leoLog = leoLog;
        this.jenkins = jenkins;
        this.buildConfig = buildConfig;
        this.leoPostBuildHandler = leoPostBuildHandler;
    }

    @Override
    public boolean tryStop() {
        StopBuildFlag buildStop = heartbeatHolder.getStopBuildFlag(leoBuild.getId());
        if (buildStop.getIsStop()) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildResult(RESULT_ERROR)
                    .endTime(new Date())
                    .isFinish(true)
                    .isActive(false)
                    .buildStatus(StringFormatter.format("{} 手动停止任务", buildStop.getUsername()))
                    .build();
            save(saveLeoBuild);
            leoLog.error(leoBuild, StringFormatter.format("{} 手动停止任务", buildStop.getUsername()));
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        setHeartbeat();
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            JobWithDetails jobWithDetails = jenkinsServer.getJob(leoBuild.getBuildJobName());
            while (true) {
                setHeartbeat();
                if (tryStop()) {
                    return;
                }
                if (this.build == null) {
                    Build build = jobWithDetails.details().getLastBuild();
                    if (build.equals(Build.BUILD_HAS_NEVER_RUN)) {
                        sleep();
                        continue;
                    }
                    if (build.equals(Build.BUILD_HAS_BEEN_CANCELLED)) {
                        LeoBuild saveLeoBuild = LeoBuild.builder()
                                .id(leoBuild.getId())
                                .endTime(new Date())
                                .isFinish(true)
                                .isActive(false)
                                .buildResult(BuildResult.CANCELLED.name())
                                .buildStatus("用户在构建引擎中取消任务！")
                                .build();
                        save(saveLeoBuild, "用户在构建引擎中取消任务！");
                        break;
                    }
                    this.build = build;
                }
                // 查询构建结果
                BuildWithDetails buildWithDetails = this.build.details();
                if (buildWithDetails.isBuilding()) {
                    sleep();
                } else {
                    // 任务正常完成
                    LeoBuild saveLeoBuild = LeoBuild.builder()
                            .id(leoBuild.getId())
                            .endTime(new Date())
                            .isFinish(true)
                            .buildResult(buildWithDetails.getResult().name())
                            .buildStatus("执行构建任务阶段: 结束")
                            .build();
                    save(saveLeoBuild, "执行构建任务阶段: 结束");
                    postBuildHandle();
                    return;
                }
                if (isTimeout()) {
                    throw new LeoBuildException("任务超时: {}", TIMEOUT);
                }
            }
        } catch (URISyntaxException | IOException | LeoBuildException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .endTime(new Date())
                    .isFinish(true)
                    .isActive(false)
                    .buildResult(RESULT_ERROR)
                    .buildStatus("监视任务阶段: 错误")
                    .build();
            save(saveLeoBuild);
            leoLog.error(leoBuild, "异常错误任务结束: {}", e.getMessage());
        }
    }

    public boolean isTimeout() {
        return com.baiyi.opscloud.common.util.NewTimeUtil.isTimeout(leoBuild.getStartTime().getTime(), TIMEOUT);
    }

    private void setHeartbeat() {
        heartbeatHolder.setHeartbeat(HeartbeatTypeConstants.BUILD, leoBuild.getId());
    }

    private void sleep() {
        NewTimeUtil.sleep(SLEEP_SECONDS);
    }

    private void postBuildHandle() {
        leoPostBuildHandler.handleBuild(leoBuild, buildConfig);
    }

    /**
     * 记录构建详情
     *
     * @param saveLeoBuild
     */
    protected void save(LeoBuild saveLeoBuild) {
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
    }

    /**
     * 记录构建详情和日志
     *
     * @param saveLeoBuild
     * @param log
     * @param var2
     */
    protected void save(LeoBuild saveLeoBuild, String log, Object... var2) {
        save(saveLeoBuild);
        leoLog.info(saveLeoBuild, log, var2);
    }

}