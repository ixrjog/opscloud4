package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseXTermMessage;
import com.baiyi.opscloud.xterm.message.DuplicateSessionMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/13 10:24 上午
 * @Version 1.0
 */
@Component
public class XTermDuplicateSessionProcess extends BaseXTermProcess implements IXTermProcess {

    /**
     * XTerm复制会话
     *
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.DUPLICATE_SESSION.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        DuplicateSessionMessage xtermMessage = (DuplicateSessionMessage) getXTermMessage(message);
        String username = ocAuthFacade.getUserByToken(xtermMessage.getToken());
        if (StringUtils.isEmpty(username)) return;
        OcUser ocUser = ocUserService.queryOcUserByUsername(username);

        JSchSession jSchSession = JSchSessionMap.getBySessionId(session.getId(), xtermMessage.getDuplicateInstanceId());

        String host = jSchSession.getHostSystem().getHost();
        boolean isAdmin = isOps(ocUser);
        HostSystem hostSystem = buildHostSystem(ocUser, host, xtermMessage, isAdmin);

        RemoteInvokeHandler.openSSHTermOnSystem(session.getId(), xtermMessage.getInstanceId(), hostSystem);
    }


    @Override
    protected BaseXTermMessage getXTermMessage(String message) {
        DuplicateSessionMessage xtermMessage = new GsonBuilder().create().fromJson(message, DuplicateSessionMessage.class);
        return xtermMessage;
    }

}
