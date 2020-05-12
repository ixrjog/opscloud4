package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserPermissionService;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.message.XTermInitialWSMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.baiyi.opscloud.xterm.task.SentOutputTask;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:36 上午
 * @Version 1.0
 */
@Component
public class XTermInitialProcess extends BaseXTermProcess implements IXTermProcess {

    // 高权限账户
    public static final String HIGH_AUTHORITY_ACCOUNT = "admin";

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    /**
     * 初始化XTerm
     *
     * @return
     */


    @Override
    public String getKey() {
        return XTermRequestStatus.INITIAL.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        XTermInitialWSMessage initialMessage = (XTermInitialWSMessage) getXTermMessage(message);
        String username = ocAuthFacade.getUserByToken(initialMessage.getToken());
        if (StringUtils.isEmpty(username)) return;
        OcUser ocUser = ocUserService.queryOcUserByUsername(username);

        BusinessWrapper wrapper = serverGroupFacade.getServerTreeHostPatternMap(initialMessage.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return;
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) wrapper.getBody();

        for (String instanceId : initialMessage.getInstanceIds()) {
            if (!serverTreeHostPatternMap.containsKey(instanceId))
                continue;
            String host = serverTreeHostPatternMap.get(instanceId);
            HostSystem hostSystem = buildHostSystem(ocUser, host, initialMessage);

            RemoteInvokeHandler.getSession(session.getId(), instanceId, hostSystem);
            Runnable run = new SentOutputTask(session.getId(), session);
            Thread thread = new Thread(run);
            thread.start();
        }
    }

    private HostSystem buildHostSystem(OcUser ocUser, String host, XTermInitialWSMessage initialMessage) {
        OcServer ocServer = ocServerService.queryOcServerByIp(host);
        OcUserPermission ocUserPermission = new OcUserPermission();
        ocUserPermission.setUserId(ocUser.getId());
        ocUserPermission.setBusinessId(ocServer.getServerGroupId());
        ocUserPermission.setBusinessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType());

        boolean loginType = false;
        if (initialMessage.getLoginUserType() == 1) {
            OcUserPermission checkUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
            if (checkUserPermission != null)
                loginType = true;
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
        hostSystem.setInitialMessage(initialMessage);

        return hostSystem;
    }


    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        XTermInitialWSMessage initialMessage = new GsonBuilder().create().fromJson(message, XTermInitialWSMessage.class);
        return initialMessage;
    }

}
