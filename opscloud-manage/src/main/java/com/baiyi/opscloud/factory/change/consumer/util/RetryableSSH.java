package com.baiyi.opscloud.factory.change.consumer.util;

import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.facade.KeyboxFacade;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/22 1:00 下午
 * @Version 1.0
 */
@Component
public class RetryableSSH {

    @Resource
    private KeyboxFacade keyboxFacade;
    /**
     * 重试30次每次间隔10s
     *
     * @param ocServer
     * @throws RuntimeException
     */
    @Retryable(value = RuntimeException.class, maxAttempts = 30, backoff = @Backoff(delay = 10000))
    public void tryServerSSH(OcServer ocServer) throws RuntimeException {
        SSHKeyCredential sshKeyCredential = keyboxFacade.getSSHKeyCredential(ocServer.getLoginUser());
        TrySSHUtils.trySSH(ocServer, sshKeyCredential);
    }

    @Retryable( maxAttempts = 4, backoff = @Backoff(delay = 2000))
    public void tryTest() throws Exception {
        System.err.println("This is Retryable Test");
        throw new Exception();
    }
}
