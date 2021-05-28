package com.baiyi.caesar.factory.terminal;

import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.redis.TerminalKeyUtil;
import com.baiyi.caesar.common.util.IOUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.bo.SSHKeyCredential;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.domain.generator.caesar.TerminalSessionInstance;
import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.service.terminal.TerminalSessionInstanceService;
import com.baiyi.caesar.service.terminal.TerminalSessionService;
import com.baiyi.caesar.terminal.config.TerminalConfig;
import com.baiyi.caesar.terminal.handler.AuditRecordHandler;
import com.baiyi.caesar.terminal.message.BaseMessage;
import com.baiyi.caesar.terminal.model.HostSystem;
import com.baiyi.caesar.terminal.model.JSchSessionMap;
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
public abstract class BaseProcess implements ITerminalProcess, InitializingBean {

    @Resource
    protected TerminalSessionService terminalSessionService;

    @Resource
    protected TerminalSessionInstanceService terminalSessionInstanceService;


    @Resource
    protected RedisUtil redisUtil;


    @Resource
    private TerminalConfig terminalConfig;


    abstract protected BaseMessage getMessage(String message);


    protected HostSystem buildHostSystem(String host, BaseMessage message) {
        // Server server = serverService.queryByIp(host);

        Server server = new Server();
        UserPermission userPermission = UserPermission.builder()
                .userId(SessionUtil.getUserId())
                .businessType(BusinessTypeEnum.SERVER_ADMINISTRATOR_ACCOUNT.getType())
                .businessId(server.getId())
                .build();


       // SSHKeyCredential sshKeyCredential = acqCredential(server, loginType);
        HostSystem hostSystem = new HostSystem();
        hostSystem.setHost(host);
        // 自定义 ssh 端口
        hostSystem.setPort(Integer.parseInt("22"));
       // hostSystem.setSshKeyCredential(sshKeyCredential);
        hostSystem.setLoginMessage(message);

        return hostSystem;
    }

    private SSHKeyCredential acqCredential(Server server, int loginType) {
//        String account;
//        if (loginType) {
//            account = serverAttributeFacade.getAdminAccount(ocServer);
//            if (StringUtils.isEmpty(account))
//                account = settingFacade.querySetting(SettingName.SERVER_HIGH_AUTHORITY_ACCOUNT);
//
//        } else {
//            account = ocServer.getLoginUser();
//        }
//        return keyboxFacade.getSSHKeyCredential(account);
        return null;

    }

    protected Boolean isBatch(TerminalSession ocTerminalSession) {
        Boolean isBatch = JSchSessionMap.getBatchBySessionId(ocTerminalSession.getSessionId());
        return isBatch == null ? false : isBatch;
    }

    protected void closeSessionInstance(TerminalSession terminalSession, String instanceId) {
        try {
            TerminalSessionInstance terminalSessionInstance =  terminalSessionInstanceService.getByUniqueKey(terminalSession.getSessionId(), instanceId);
            terminalSessionInstance.setCloseTime((new Date()));
            terminalSessionInstance.setInstanceClosed(true);
            terminalSessionInstance.setOutputSize(IOUtil.fileSize(terminalConfig.getAuditLogPath(terminalSession.getSessionId(), instanceId)));
            terminalSessionInstanceService.update(terminalSessionInstance);
        } catch (Exception ignored) {
        }
    }

    protected void writeAuditLog(TerminalSession terminalSession, String instanceId) {
        AuditRecordHandler.recordAuditLog(terminalSession.getSessionId(), instanceId);
    }

    protected void writeCommanderLog(StringBuffer commander, TerminalSession terminalSession, String instanceId) {
        AuditRecordHandler.recordCommanderLog(commander, terminalSession.getSessionId(), instanceId);
    }

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
