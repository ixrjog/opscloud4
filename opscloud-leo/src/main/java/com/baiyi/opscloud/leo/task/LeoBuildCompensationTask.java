package com.baiyi.opscloud.leo.task;

import com.baiyi.opscloud.common.instance.OcInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.helper.BuildingLogHelper;
import com.baiyi.opscloud.leo.helper.LeoHeartbeatHelper;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

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

    private final LeoHeartbeatHelper heartbeatHelper;

    private final BuildingLogHelper logHelper;

    public void handleTask() {
        List<LeoBuild> leoBuilds = leobuildService.queryNotFinishBuildWithOcInstance(OcInstance.ocInstance);
        if (CollectionUtils.isEmpty(leoBuilds)) {
            return;
        }
        leoBuilds.forEach(leoBuild -> {
            if (!heartbeatHelper.isLive(LeoHeartbeatHelper.HeartbeatTypes.BUILD, leoBuild.getId())) {
                LeoBuild saveLeoBuild = LeoBuild.builder()
                        .buildResult("ERROR")
                        .endTime(new Date())
                        .isActive(false)
                        .isFinish(true)
                        .build();
                leobuildService.updateByPrimaryKeySelective(saveLeoBuild);
                logHelper.error(leoBuild,"任务异常终止: 心跳丢失！");
            }
        });
    }

}
