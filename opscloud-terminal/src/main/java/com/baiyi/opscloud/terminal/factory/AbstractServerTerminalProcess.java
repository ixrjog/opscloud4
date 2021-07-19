package com.baiyi.opscloud.terminal.factory;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.redis.TerminalKeyUtil;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.base.ITerminalProcess;
import com.baiyi.opscloud.sshcore.config.TerminalConfig;
import com.baiyi.opscloud.sshcore.handler.AuditRecordHandler;
import com.baiyi.opscloud.sshcore.handler.HostSystemHandler;
import com.baiyi.opscloud.sshcore.message.server.BaseServerMessage;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Date;


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
    private TerminalConfig terminalConfig;

    @Resource
    protected HostSystemHandler hostSystemHandler;

    abstract protected T getMessage(String message);

    protected Boolean isBatch(TerminalSession terminalSession) {
        Boolean isBatch = JSchSessionContainer.getBatchBySessionId(terminalSession.getSessionId());
        return isBatch == null ? false : isBatch;
    }

    protected void closeSessionInstance(TerminalSession terminalSession, String instanceId) {
        TerminalSessionInstance terminalSessionInstance = terminalSessionInstanceService.getByUniqueKey(terminalSession.getSessionId(), instanceId);
        terminalSessionInstance.setCloseTime((new Date()));
        terminalSessionInstance.setInstanceClosed(true);
        terminalSessionInstance.setOutputSize(IOUtil.fileSize(terminalConfig.buildAuditLogPath(terminalSession.getSessionId(), instanceId)));
        terminalSessionInstanceService.update(terminalSessionInstance);
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
