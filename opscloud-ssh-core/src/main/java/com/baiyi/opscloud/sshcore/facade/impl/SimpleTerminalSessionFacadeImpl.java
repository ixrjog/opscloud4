package com.baiyi.opscloud.sshcore.facade.impl;

import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.audit.ServerCommandAudit;
import com.baiyi.opscloud.sshcore.config.TerminalConfigurationProperties;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/21 2:34 下午
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleTerminalSessionFacadeImpl implements SimpleTerminalSessionFacade {

    private final TerminalConfigurationProperties terminalConfig;

    private final TerminalSessionService terminalSessionService;

    private final TerminalSessionInstanceService terminalSessionInstanceService;

    private final ServerCommandAudit serverCommandAudit;

    @Override
    public void closeTerminalSessionInstance(TerminalSessionInstance terminalSessionInstance) {
        serverCommandAudit.recordCommand(terminalSessionInstance);
        terminalSessionInstance.setCloseTime((new Date()));
        terminalSessionInstance.setInstanceClosed(true);
        terminalSessionInstance.setOutputSize(IOUtil.fileSize(terminalConfig.buildAuditLogPath(terminalSessionInstance.getSessionId(), terminalSessionInstance.getInstanceId())));
        terminalSessionInstanceService.update(terminalSessionInstance);
    }

    @Override
    @Retryable(retryFor = RetryException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void closeTerminalSessionInstance(TerminalSession terminalSession, String instanceId) throws RetryException {
        TerminalSessionInstance terminalSessionInstance = terminalSessionInstanceService.getByUniqueKey(terminalSession.getSessionId(), instanceId);
        if (terminalSessionInstance == null) {
            log.error("实例未完成初始化用户就退出了: sessionId={}, instanceId={}", terminalSession.getSessionId(), instanceId);
            throw new RetryException("实例未完成初始化用户就退出了: sessionId=" + terminalSession.getSessionId());
        }
        if (terminalSessionInstance.getInstanceClosed()) {
            return;
        }
        closeTerminalSessionInstance(terminalSessionInstance);
    }

    @Override
    public void closeTerminalSession(TerminalSession terminalSession) {
        terminalSession.setCloseTime(new Date());
        terminalSession.setSessionClosed(true);
        terminalSessionService.update(terminalSession);
    }

    @Override
    public void recordTerminalSessionInstance(TerminalSessionInstance terminalSessionInstance) {
        terminalSessionInstanceService.add(terminalSessionInstance);
    }

    @Override
    public void recordTerminalSession(TerminalSession terminalSession) {
        terminalSessionService.add(terminalSession);
    }

    @Override
    public TerminalSession getTerminalSessionBySessionId(String sessionId) {
        return terminalSessionService.getBySessionId(sessionId);
    }

    @Override
    public void closeTerminalSessionById(int id) {
        TerminalSession terminalSession = terminalSessionService.getById(id);
        if (terminalSession.getSessionClosed()) {
            return;
        }
        List<TerminalSessionInstance> instances = terminalSessionInstanceService.queryBySessionId(terminalSession.getSessionId());
        if (!CollectionUtils.isEmpty(instances)) {
            for (TerminalSessionInstance instance : instances) {
                closeTerminalSessionInstance(instance);
            }
        }
        closeTerminalSession(terminalSession);
    }

}