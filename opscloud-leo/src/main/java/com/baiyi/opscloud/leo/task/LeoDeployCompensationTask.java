package com.baiyi.opscloud.leo.task;

import com.baiyi.opscloud.common.instance.OcInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.HeartbeatTypeConstants;
import com.baiyi.opscloud.leo.handler.deploy.BaseDeployChainHandler;
import com.baiyi.opscloud.leo.helper.DeployingLogHelper;
import com.baiyi.opscloud.leo.helper.LeoHeartbeatHelper;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/26 13:48
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoDeployCompensationTask {

    private final LeoDeployService deployService;

    private final LeoHeartbeatHelper heartbeatHelper;

    private final DeployingLogHelper logHelper;

    public void handleTask() {
        List<LeoDeploy> leoDeploys = deployService.queryNotFinishDeployWithOcInstance(OcInstance.OC_INSTANCE);
        if (CollectionUtils.isEmpty(leoDeploys)) {
            return;
        }
        leoDeploys.forEach(leoDeploy -> {
            if (!heartbeatHelper.isLive(HeartbeatTypeConstants.DEPLOY, leoDeploy.getId())) {
                LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                        .id(leoDeploy.getId())
                        .deployResult(BaseDeployChainHandler.RESULT_ERROR)
                        .endTime(new Date())
                        .isFinish(true)
                        .isActive(false)
                        .deployStatus("任务异常终止,心跳丢失！")
                        .build();
                deployService.updateByPrimaryKeySelective(saveLeoDeploy);
                logHelper.error(leoDeploy,"任务异常终止,心跳丢失！");
            }
        });
    }

}
