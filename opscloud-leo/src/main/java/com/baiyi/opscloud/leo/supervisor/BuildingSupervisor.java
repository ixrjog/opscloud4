package com.baiyi.opscloud.leo.supervisor;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.model.Build;
import com.baiyi.opscloud.datasource.jenkins.model.BuildResult;
import com.baiyi.opscloud.datasource.jenkins.model.BuildWithDetails;
import com.baiyi.opscloud.datasource.jenkins.model.JobWithDetails;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.log.BuildingLogHelper;
import com.baiyi.opscloud.leo.supervisor.base.ISupervisor;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2022/11/9 11:43
 * @Version 1.0
 */
@Slf4j
public class BuildingSupervisor implements ISupervisor {

    private static final int SLEEP_SECONDS = 8;

    private final LeoBuild leoBuild;

    private final LeoBuildService leoBuildService;

    private final BuildingLogHelper logHelper;

    private final JenkinsConfig.Jenkins jenkins;

    private Build build;

    public BuildingSupervisor(LeoBuildService leoBuildService,
                              LeoBuild leoBuild,
                              BuildingLogHelper logHelper,
                              JenkinsConfig.Jenkins jenkins) {
        this.leoBuildService = leoBuildService;
        this.leoBuild = leoBuild;
        this.logHelper = logHelper;
        this.jenkins = jenkins;
    }

    @Override
    public void run() {
        try {
            JobWithDetails jobWithDetails = JenkinsServerDriver.getJob(jenkins, leoBuild.getBuildJobName());
            while (true) {
                if (this.build == null) {
                    Build build = JenkinsServerDriver.getLastBuildWithJob(jobWithDetails);
                    if (build.equals(Build.BUILD_HAS_NEVER_RUN)) {
                        // 未运行或在队列中
                        TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
                        continue;
                    }
                    if (build.equals(Build.BUILD_HAS_BEEN_CANCELLED)) {
                        logHelper.info(leoBuild, "用户取消任务！");
                        LeoBuild saveLeoBuild = LeoBuild.builder()
                                .id(leoBuild.getId())
                                .endTime(new Date())
                                .isFinish(true)
                                .buildResult(BuildResult.CANCELLED.name())
                                .buildStatus("用户取消任务")
                                .build();
                        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
                        break;
                    }
                    this.build = build;
                }
                // 查询构建结果
                BuildWithDetails buildWithDetails = build.details();
                if (buildWithDetails.isBuilding()) {
                    TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
                } else {
                    logHelper.info(leoBuild, "执行构建任务阶段: 结束");
                    LeoBuild saveLeoBuild = LeoBuild.builder()
                            .id(leoBuild.getId())
                            .endTime(new Date())
                            .isFinish(true)
                            .buildResult(buildWithDetails.getResult().name())
                            .buildStatus("执行构建任务阶段: 结束")
                            .build();
                    leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
                }
            }
        } catch (Exception e) {
            logHelper.error(leoBuild, "异常错误任务结束: err={}", e.getMessage());
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .endTime(new Date())
                    .isFinish(true)
                    .buildResult("ERROR")
                    .buildStatus("监视任务阶段: 异常")
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
        }
    }

}
