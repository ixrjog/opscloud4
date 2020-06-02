package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.bae64.CacheKeyUtils;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.facade.*;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.factory.xterm.XTermProcessFactory;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserPermissionService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.xterm.handler.AuditLogHandler;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Date;

import static com.baiyi.opscloud.common.base.Global.HIGH_AUTHORITY_ACCOUNT;


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

    abstract protected BaseMessage getXTermMessage(String message);

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
            if (isAdmin) {
                loginType = true;
            } else {
                OcUserPermission checkUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
                if (checkUserPermission != null)
                    loginType = true;
            }
        }

        SSHKeyCredential sshKeyCredential;
        if (loginType) {
            sshKeyCredential = keyboxFacade.getSSHKeyCredential(HIGH_AUTHORITY_ACCOUNT); // 高权限
        } else {
            sshKeyCredential = keyboxFacade.getSSHKeyCredential(ocServer.getLoginUser());  // 普通用户
        }
        HostSystem hostSystem = new HostSystem();
        hostSystem.setHost(host);
        hostSystem.setSshKeyCredential(sshKeyCredential);
        hostSystem.setInitialMessage(baseMessage);

        return hostSystem;
    }

    protected Boolean isBatch(OcTerminalSession ocTerminalSession) {
        Boolean isBatch = JSchSessionMap.getBatchBySessionId(ocTerminalSession.getSessionId());
        if (isBatch == null)
            isBatch = Boolean.FALSE;
        return isBatch;
    }

    protected void sessionInstanceClosed(OcTerminalSession ocTerminalSession, String instanceId) {
        try {
            OcTerminalSessionInstance ocTerminalSessionInstance = terminalFacade.queryOcTerminalSessionInstanceByUniqueKey(ocTerminalSession.getSessionId(), instanceId);
            ocTerminalSessionInstance.setCloseTime(new Date());
            ocTerminalSessionInstance.setIsClosed(true);
            terminalFacade.updateOcTerminalSessionInstance(ocTerminalSessionInstance);
        } catch (Exception e) {

        }
    }

    protected void writeAuditLog(OcTerminalSession ocTerminalSession, String instanceId) {
        AuditLogHandler.writeAuditLog(ocTerminalSession.getSessionId(), instanceId);
    }

    protected void heartbeat(String sessionId){
        redisUtil.set(CacheKeyUtils.getTermSessionHeartbeatKey(sessionId), true, 60 * 1000L);
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        XTermProcessFactory.register(this);
    }

}
