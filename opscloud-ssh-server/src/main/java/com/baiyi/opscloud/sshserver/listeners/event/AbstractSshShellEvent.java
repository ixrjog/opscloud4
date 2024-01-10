package com.baiyi.opscloud.sshserver.listeners.event;

import com.baiyi.opscloud.common.model.HostInfo;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionBuilder;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshcore.model.SessionIdMapper;
import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import jakarta.annotation.Resource;
import org.apache.sshd.common.session.SessionContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * @Author baiyi
 * @Date 2022/8/30 16:16
 * @Version 1.0
 */
@Component
public abstract class AbstractSshShellEvent implements ISshShellEvent, InitializingBean {

    public final static HostInfo HOST_INFO = HostInfo.build();

    @Resource
    private SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    protected void openTerminalSession(SshShellEvent event) {
        String sessionId = SessionIdMapper.getSessionId(event.getSession().getServerSession().getIoSession());
        SessionContext sc = event.getSession().getSessionContext();
        TerminalSession terminalSession = TerminalSessionBuilder.build(
                sessionId,
                event.getSession().getServerSession().getUsername(),
                HOST_INFO,
                sc.getRemoteAddress(),
                SessionTypeEnum.SSH_SERVER);
        simpleTerminalSessionFacade.recordTerminalSession(terminalSession);
    }

    protected void closeTerminalSession(SshShellEvent event) {
        String sessionId = SessionIdMapper.getSessionId(event.getSession().getServerSession().getIoSession());
        TerminalSession terminalSession = simpleTerminalSessionFacade.getTerminalSessionBySessionId(sessionId);
        simpleTerminalSessionFacade.closeTerminalSessionById(terminalSession.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SshShellEventFactory.register(this);
    }

}