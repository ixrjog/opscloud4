package com.baiyi.opscloud.sshcore.facade.impl;

import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.config.TerminalConfigurationProperties;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/7/21 2:34 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SimpleTerminalSessionFacadeImpl implements SimpleTerminalSessionFacade {

    private final TerminalConfigurationProperties terminalConfig;

    private final TerminalSessionService terminalSessionService;

    private final TerminalSessionInstanceService terminalSessionInstanceService;

    @Override
    public void closeTerminalSessionInstance(TerminalSessionInstance terminalSessionInstance){
        terminalSessionInstance.setCloseTime((new Date()));
        terminalSessionInstance.setInstanceClosed(true);
        terminalSessionInstance.setOutputSize(IOUtil.fileSize(terminalConfig.buildAuditLogPath(terminalSessionInstance.getSessionId(), terminalSessionInstance.getInstanceId())));
        terminalSessionInstanceService.update(terminalSessionInstance);
    }

    @Override
    public void closeTerminalSessionInstance(TerminalSession terminalSession, String instanceId) {
        TerminalSessionInstance terminalSessionInstance = terminalSessionInstanceService.getByUniqueKey(terminalSession.getSessionId(), instanceId);
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

}
