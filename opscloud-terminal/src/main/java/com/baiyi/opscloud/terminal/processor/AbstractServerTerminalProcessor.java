package com.baiyi.opscloud.terminal.processor;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.sshcore.audit.ServerCommandAudit;
import com.baiyi.opscloud.sshcore.ITerminalProcessor;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshcore.handler.HostSystemHandler;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.terminal.factory.TerminalProcessFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;


/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractServerTerminalProcessor<T extends ServerMessage.BaseMessage> implements ITerminalProcessor, InitializingBean {

    @Resource
    protected ServerCommandAudit serverCommandAudit;

    @Resource
    protected TerminalSessionInstanceService terminalSessionInstanceService;

    @Resource
    protected HostSystemHandler hostSystemHandler;

    @Resource
    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    abstract protected T getMessage(String message);

    protected Boolean needBatch(TerminalSession terminalSession) {
        Boolean needBatch = JSchSessionContainer.getBatchBySessionId(terminalSession.getSessionId());
        return needBatch != null && needBatch;
    }

    protected void heartbeat(String sessionId) {
       // redisUtil.set(TerminalKeyUtil.buildSessionHeartbeatKey(sessionId), true, 60L);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        TerminalProcessFactory.register(this);
    }

}
