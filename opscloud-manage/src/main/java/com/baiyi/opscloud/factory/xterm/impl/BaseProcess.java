package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.SettingName;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.common.util.bae64.CacheKeyUtils;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.facade.*;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.factory.xterm.XTermProcessFactory;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserPermissionService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.xterm.config.XTermConfig;
import com.baiyi.opscloud.xterm.handler.AuditLogHandler;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
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
public abstract class BaseProcess implements IXTermProcess, InitializingBean {

    @Resource
    protected UserFacade userFacade;

    @Resource
    protected UserPermissionFacade userPermissionFacade;

    @Resource
    protected OcUserService ocUserService;

    @Resource
    protected ServerGroupFacade serverGroupFacade;

    @Resource
    protected KeyboxFacade keyboxFacade;

    @Resource
    protected OcServerService ocServerService;

    @Resource
    protected OcUserPermissionService ocUserPermissionService;

    @Resource
    protected TerminalBaseFacade terminalFacade;

    @Resource
    protected RedisUtil redisUtil;

    @Resource
    protected SettingBaseFacade settingFacade;

    @Resource
    private XTermConfig xtermConfig;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    abstract protected BaseMessage getMessage(String message);

    /**
     * 判断用户访问级别 >= ops
     *
     * @param ocUser
     * @return
     */
    protected boolean isOps(OcUser ocUser) {
        return userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.OPS.getLevel()).isSuccess();
    }

    protected HostSystem buildHostSystem(OcUser ocUser, String host, BaseMessage baseMessage, boolean isAdmin) {
        OcServer ocServer = ocServerService.queryOcServerByIp(host);
        OcUserPermission ocUserPermission = new OcUserPermission();
        ocUserPermission.setUserId(ocUser.getId());
        ocUserPermission.setBusinessId(ocServer.getServerGroupId());
        ocUserPermission.setBusinessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType());

        boolean loginType = false;
        if (baseMessage.getLoginUserType() == 1) {
            loginType = isAdmin || ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission) != null;
        }

        SSHKeyCredential sshKeyCredential = loginType ? keyboxFacade.getSSHKeyCredential(settingFacade.querySetting(SettingName.SERVER_HIGH_AUTHORITY_ACCOUNT))
                : keyboxFacade.getSSHKeyCredential(ocServer.getLoginUser());

        HostSystem hostSystem = new HostSystem();
        hostSystem.setHost(host);
        // 自定义 ssh 端口
        hostSystem.setPort(Integer.parseInt(serverAttributeFacade.getSSHPort(ocServer)));
        hostSystem.setSshKeyCredential(sshKeyCredential);
        hostSystem.setInitialMessage(baseMessage);

        return hostSystem;
    }

    protected Boolean isBatch(OcTerminalSession ocTerminalSession) {
        Boolean isBatch = JSchSessionMap.getBatchBySessionId(ocTerminalSession.getSessionId());
        return isBatch == null ? false : isBatch;
    }

    protected void closeSessionInstance(OcTerminalSession ocTerminalSession, String instanceId) {
        try {
            OcTerminalSessionInstance ocTerminalSessionInstance = terminalFacade.queryOcTerminalSessionInstanceByUniqueKey(ocTerminalSession.getSessionId(), instanceId);
            ocTerminalSessionInstance.setCloseTime(new Date());
            ocTerminalSessionInstance.setIsClosed(true);
            ocTerminalSessionInstance.setOutputSize(IOUtils.fileSize(xtermConfig.getAuditLogPath(ocTerminalSession.getSessionId(), instanceId)));
            terminalFacade.updateOcTerminalSessionInstance(ocTerminalSessionInstance);
        } catch (Exception ignored) {
        }
    }

    protected void writeAuditLog(OcTerminalSession ocTerminalSession, String instanceId) {
        AuditLogHandler.writeAuditLog(ocTerminalSession.getSessionId(), instanceId);
    }

    protected void writeCommanderLog(StringBuffer commander, OcTerminalSession ocTerminalSession, String instanceId) {
        AuditLogHandler.writeCommanderLog(commander, ocTerminalSession.getSessionId(), instanceId);
    }

    protected void heartbeat(String sessionId) {
        redisUtil.set(CacheKeyUtils.getTermSessionHeartbeatKey(sessionId), true, 60L);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        XTermProcessFactory.register(this);
    }

}
