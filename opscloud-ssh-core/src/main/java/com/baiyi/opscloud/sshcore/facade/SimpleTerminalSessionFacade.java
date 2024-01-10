package com.baiyi.opscloud.sshcore.facade;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;

/**
 * @Author baiyi
 * @Date 2021/7/21 2:33 下午
 * @Version 1.0
 */
public interface SimpleTerminalSessionFacade {

    void closeTerminalSessionInstance(TerminalSessionInstance terminalSessionInstance);

    void closeTerminalSessionInstance(TerminalSession terminalSession, String instanceId);

    void closeTerminalSession(TerminalSession terminalSession);

    void recordTerminalSessionInstance(TerminalSessionInstance terminalSessionInstance);

    void recordTerminalSession(TerminalSession terminalSession);

    TerminalSession getTerminalSessionBySessionId(String sessionId);

    void closeTerminalSessionById(int id);

}