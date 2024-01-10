package com.baiyi.opscloud.leo.task;

import com.baiyi.opscloud.common.instance.OcInstance;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.constants.HeartbeatTypeConstants;
import com.baiyi.opscloud.leo.log.LeoBuildingLog;
import com.baiyi.opscloud.leo.holder.LeoHeartbeatHolder;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

import static com.baiyi.opscloud.leo.task.LeoDeployCompensationTask.MAX_HEARTBEAT_DELAY;

/**
 * @Author baiyi
 * @Date 2023/1/4 16:02
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoBuildCompensationTask {

    private final LeoBuildService leobuildService;

    private final LeoHeartbeatHolder heartbeatHolder;

    private final LeoBuildingLog leoLog;

    public void handleTask() {
        List<LeoBuild> leoBuilds = leobuildService.queryNotFinishBuildWithOcInstance(OcInstance.OC_INSTANCE);
        if (CollectionUtils.isEmpty(leoBuilds)) {
            return;
        }
        leoBuilds.forEach(leoBuild -> {
            if (!heartbeatHolder.isLive(HeartbeatTypeConstants.BUILD, leoBuild.getId())) {
                if (leoBuild.getStartTime() != null && NewTimeUtil.calculateHowManySecondsHavePassed(leoBuild.getStartTime()) >= MAX_HEARTBEAT_DELAY) {
                    LeoBuild saveLeoBuild = LeoBuild.builder()
                            .buildResult("ERROR")
                            .endTime(new Date())
                            .isActive(false)
                            .isFinish(true)
                            .buildStatus("Compensation task: 心跳丢失任务异常终止！")
                            .build();
                    leobuildService.updateByPrimaryKeySelective(saveLeoBuild);
                    leoLog.error(leoBuild, "Compensation task: 心跳丢失任务异常终止！");
                }
            }
        });
    }

}