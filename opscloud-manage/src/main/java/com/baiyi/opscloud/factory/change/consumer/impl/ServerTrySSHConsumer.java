package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import com.baiyi.opscloud.factory.change.consumer.bo.ChangeResult;
import com.baiyi.opscloud.factory.change.consumer.util.RetryableSSH;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/18 9:33 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerTrySSHConsumer extends BaseServerChangeConsumer implements IServerChangeConsumer {

    @Resource
    private RetryableSSH retryableSSH;

    @Override
    public String getKey() {
        return ServerChangeFlow.SERVER_TRY_SSH.getName();
    }

    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        OcServer ocServer = getServer(ocServerChangeTask);
        saveChangeTaskFlowStart(ocServerChangeTaskFlow); // 任务开始
        try {
            retryableSSH.tryServerSSH(ocServer);
            saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow); // 任务结束
        } catch (RuntimeException e) {
            ChangeResult changeResult = ChangeResult.builder()
                    .msg(e.getMessage())
                    .build();
            saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow, changeResult); // 任务结束
        }
        return BusinessWrapper.SUCCESS;
    }


}
