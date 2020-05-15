package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.message.XTermInitialIpWSMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/15 2:59 下午
 * @Version 1.0
 */
@Component
public class XTermInitialIpProcess extends BaseXTermProcess implements IXTermProcess {


    /**
     * 初始化XTerm
     *
     * @return
     */
    @Override
    public String getKey() {
        return XTermRequestStatus.INITIAL_IP.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        XTermInitialIpWSMessage baseMessage = (XTermInitialIpWSMessage) getXTermMessage(message);
        baseMessage.setLoginUserType(1);

        String username = ocAuthFacade.getUserByToken(baseMessage.getToken());
        if (StringUtils.isEmpty(username)) return;
        OcUser ocUser = ocUserService.queryOcUserByUsername(username);
        String ip = baseMessage.getIp();

        boolean isAdmin = isOps(ocUser);
        // 鉴权
        if(!isAdmin){
            OcServer ocServer = ocServerService.queryOcServerByIp(ip);
            OcUserPermission ocUserPermission = new OcUserPermission();
            ocUserPermission.setUserId(ocUser.getId());
            ocUserPermission.setBusinessId(ocServer.getServerGroupId());
            ocUserPermission.setBusinessType(BusinessType.SERVERGROUP.getType());
            OcUserPermission checkUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
            if(checkUserPermission == null)
                return;
        }

        HostSystem hostSystem = buildHostSystem(ocUser, ip, baseMessage, isAdmin);
        RemoteInvokeHandler.openSSHTermOnSystem(session.getId(), baseMessage.getInstanceId(), hostSystem);
    }


    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        XTermInitialIpWSMessage baseMessage = new GsonBuilder().create().fromJson(message, XTermInitialIpWSMessage.class);
        return baseMessage;
    }

}
