package com.baiyi.opscloud.terminal.factory;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.redis.TerminalKeyUtil;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.base.ITerminalProcess;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshcore.handler.AuditRecordHandler;
import com.baiyi.opscloud.sshcore.handler.HostSystemHandler;
import com.baiyi.opscloud.sshcore.message.server.BaseServerMessage;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;


/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractServerTerminalProcess<T extends BaseServerMessage> implements ITerminalProcess, InitializingBean {

    @Resource
    protected TerminalSessionService terminalSessionService;

    @Resource
    protected TerminalSessionInstanceService terminalSessionInstanceService;

    @Resource
    protected RedisUtil redisUtil;

    @Resource
    protected HostSystemHandler hostSystemHandler;

    @Resource
    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    abstract protected T getMessage(String message);

    protected Boolean isBatch(TerminalSession terminalSession) {
        Boolean isBatch = JSchSessionContainer.getBatchBySessionId(terminalSession.getSessionId());
        return isBatch == null ? false : isBatch;
    }

    protected void recordAuditLog(TerminalSession terminalSession, String instanceId) {
        AuditRecordHandler.recordAuditLog(terminalSession.getSessionId(), instanceId);
    }

//    protected void recordCommanderLog(StringBuffer commander, TerminalSession terminalSession, String instanceId) {
//        AuditRecordHandler.recordCommanderLog(commander, terminalSession.getSessionId(), instanceId);
//    }

    protected void heartbeat(String sessionId) {
        redisUtil.set(TerminalKeyUtil.buildSessionHeartbeatKey(sessionId), true, 60L);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        TerminalProcessFactory.register(this);
    }

}
