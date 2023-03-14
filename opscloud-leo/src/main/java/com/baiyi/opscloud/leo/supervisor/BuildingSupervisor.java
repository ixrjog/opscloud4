package com.baiyi.opscloud.leo.supervisor;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.model.Build;
import com.baiyi.opscloud.datasource.jenkins.model.BuildResult;
import com.baiyi.opscloud.datasource.jenkins.model.BuildWithDetails;
import com.baiyi.opscloud.datasource.jenkins.model.JobWithDetails;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.action.build.LeoPostBuildHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.helper.BuildingLogHelper;
import com.baiyi.opscloud.leo.helper.LeoHeartbeatHelper;
import com.baiyi.opscloud.leo.supervisor.base.ISupervisor;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.baiyi.opscloud.leo.action.build.BaseBuildHandler.RESULT_ERROR;

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

    private final LeoBuildService leoBuildService;

    private final BuildingLogHelper logHelper;

    private final JenkinsConfig.Jenkins jenkins;

    private final LeoPostBuildHandler leoPostBuildHandler;

    private final LeoHeartbeatHelper heartbeatHelper;

    private final LeoBuildModel.BuildConfig buildConfig;

    private Build build;

    private final LeoBuild leoBuild;

    public BuildingSupervisor(LeoHeartbeatHelper heartbeatHelper,
                              LeoBuildService leoBuildService,
                              LeoBuild leoBuild,
                              BuildingLogHelper logHelper,
                              JenkinsConfig.Jenkins jenkins,
                              LeoBuildModel.BuildConfig buildConfig,
                              LeoPostBuildHandler leoPostBuildHandler) {
        this.heartbeatHelper = heartbeatHelper;
        this.leoBuildService = leoBuildService;
        this.leoBuild = leoBuild;
        this.logHelper = logHelper;
        this.jenkins = jenkins;
        this.buildConfig = buildConfig;
        this.leoPostBuildHandler = leoPostBuildHandler;
    }

    @Override
    public void run() {
        setHeartbeat();

        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            JobWithDetails jobWithDetails = jenkinsServer.getJob(leoBuild.getBuildJobName());
            while (true) {
                setHeartbeat();

                if (this.build == null) {
                    Build build = jobWithDetails.details().getLastBuild();
                    if (build.equals(Build.BUILD_HAS_NEVER_RUN)) {
                        try {
                            TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
                        } catch (InterruptedException ie) {
                        }
                        continue;
                    }
                    if (build.equals(Build.BUILD_HAS_BEEN_CANCELLED)) {
                        LeoBuild saveLeoBuild = LeoBuild.builder()
                                .id(leoBuild.getId())
                                .endTime(new Date())
                                .isFinish(true)
                                .buildResult(BuildResult.CANCELLED.name())
                                .buildStatus("用户取消任务！")
                                .build();
                        save(saveLeoBuild, "用户取消任务！");
                        break;
                    }
                    this.build = build;
                }
                // 查询构建结果
                BuildWithDetails buildWithDetails = this.build.details();
                if (buildWithDetails.isBuilding()) {
                    try {
                        TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
                    } catch (InterruptedException ie) {
                    }
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
            }
        } catch (URISyntaxException | IOException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .endTime(new Date())
                    .isFinish(true)
                    .isActive(false)
                    .buildResult(RESULT_ERROR)
                    .buildStatus("监视任务阶段: 错误")
                    .build();
            save(saveLeoBuild);
            logHelper.error(leoBuild, "异常错误任务结束: {}", e.getMessage());
        }
    }

    private void setHeartbeat() {
        heartbeatHelper.setHeartbeat(LeoHeartbeatHelper.HeartbeatTypes.BUILD, leoBuild.getId());
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
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
        logHelper.info(saveLeoBuild, log, var2);
    }

}
