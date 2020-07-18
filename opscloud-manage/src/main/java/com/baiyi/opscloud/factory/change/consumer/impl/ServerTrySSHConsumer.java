package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.common.base.ServerChangeFlow;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.facade.KeyboxFacade;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import com.baiyi.opscloud.factory.change.consumer.TrySSHUtils;
import com.baiyi.opscloud.factory.change.consumer.bo.ChangeResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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
    private KeyboxFacade keyboxFacade;

    @Override
    public String getKey() {
        return ServerChangeFlow.SERVER_POWER_ON.getName();
    }

    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        OcServer ocServer = getServer(ocServerChangeTask);
        saveChangeTaskFlowStart(ocServerChangeTaskFlow); // 任务开始
        try {
            tryServerSSH(ocServer);
            saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow); // 任务结束
        } catch (RuntimeException e) {
            ChangeResult changeResult = ChangeResult.builder()
                    .msg(e.getMessage())
                    .build();
            saveChangeTaskFlowEnd(ocServerChangeTask, ocServerChangeTaskFlow, changeResult); // 任务结束
        }
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 重试30次每次间隔5s
     *
     * @param ocServer
     * @throws RuntimeException
     */
    @Retryable(value = RuntimeException.class, maxAttempts = 30, backoff = @Backoff(delay = 5000))
    private void tryServerSSH(OcServer ocServer) throws RuntimeException {
        SSHKeyCredential sshKeyCredential = keyboxFacade.getSSHKeyCredential(ocServer.getLoginUser());
        TrySSHUtils.trySSH(ocServer, sshKeyCredential);
    }

}
