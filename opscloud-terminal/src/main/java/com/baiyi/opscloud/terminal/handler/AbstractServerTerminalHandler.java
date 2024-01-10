package com.baiyi.opscloud.terminal.handler;

import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.sshcore.ITerminalMessageHandler;
import com.baiyi.opscloud.sshcore.audit.ServerCommandAudit;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshcore.handler.HostSystemHandler;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.terminal.factory.ServerTerminalMessageHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;


/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractServerTerminalHandler<T extends ServerMessage.BaseMessage> implements ITerminalMessageHandler, InitializingBean {

    @Resource
    protected ServerCommandAudit serverCommandAudit;

    @Resource
    protected TerminalSessionInstanceService terminalSessionInstanceService;

    @Resource
    protected HostSystemHandler hostSystemHandler;

    @Resource
    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    /**
     * 转换消息
     * @param message
     * @return
     */
    abstract protected T toMessage(String message);

    protected void heartbeat(String sessionId) {
       // redisUtil.set(TerminalKeyUtil.buildSessionHeartbeatKey(sessionId), true, 60L);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        ServerTerminalMessageHandlerFactory.register(this);
    }

}