package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.common.base.CloudServerKey;
import com.baiyi.opscloud.common.base.CloudServerPowerStatus;
import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import com.baiyi.opscloud.factory.change.consumer.bo.ChangeResult;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2020/6/1 9:23 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerPowerOffConsumer extends BaseServerChangeConsumer implements IServerChangeConsumer {

    private static final long TASK_TIMEOUT = 2 * 60 * 1000L;

    @Resource
    private OcCloudServerService ocCloudServerService;

    @Override
    public String getKey() {
        return ServerChangeFlow.SERVER_POWER_OFF.getName();
    }

    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        OcServer ocServer = getServer(ocServerChangeTask);
        saveChangeTaskFlowStart(ocServerChangeTaskFlow); // 任务开始

        String cloudServerKey = CloudServerKey.getKey(ocServer.getServerType());
        ICloudServer iCloudServer = CloudServerFactory.getCloudServerByKey(cloudServerKey);

        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByUnqueKey(ocServer.getServerType(), ocServer.getId());
        if (ocCloudServer == null) {
            ChangeResult changeResult = ChangeResult.builder().build();
            saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow, changeResult); // 任务结束
            return new BusinessWrapper<>(ErrorEnum.CLOUD_SERVER_NOT_EXIST);
        }
        if (!BooleanUtils.isTrue(ocCloudServer.getPowerMgmt())) { // 允许电源管理
            ocCloudServer.setPowerMgmt(true);
            ocCloudServerService.updateOcCloudServer(ocCloudServer);
        }

        ocServerChangeTaskFlow.setExternalId(ocCloudServer.getId());
        ocServerChangeTaskFlow.setExternalType(cloudServerKey);
        updateOcServerChangeTaskFlow(ocServerChangeTaskFlow);

        long startTaskTime = new Date().getTime();
        iCloudServer.stop(ocCloudServer.getId()); // 停止实例

        while (true) {
            try {
                if (TimeUtils.checkTimeout(startTaskTime, TASK_TIMEOUT)) {
                    ChangeResult changeResult = ChangeResult.builder()
                            .msg(Joiner.on("").join("TASK_TIMEOUT:", (TASK_TIMEOUT / 1000), "s"))
                            .build();
                    saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow, changeResult); // 任务结束
                    return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_TIMEOUT);
                }

                TimeUnit.SECONDS.sleep(5); // 5秒延迟
                int powerStatus = iCloudServer.queryPowerStatus(ocCloudServer.getId());
                if (powerStatus == CloudServerPowerStatus.STOPPED.getStatus() || powerStatus == CloudServerPowerStatus.STOPPING.getStatus()) { //  已关闭
                    saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow);
                    break;
                }
            } catch (InterruptedException ignored) {
            }
        }

        return BusinessWrapper.SUCCESS;
    }


}
