package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import com.baiyi.opscloud.xterm.message.DuplicateSessionMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.baiyi.opscloud.xterm.model.JSchSession;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/13 10:24 上午
 * @Version 1.0
 */
@Component
public class DuplicateSessionProcess extends BaseProcess implements IXTermProcess {

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
    public void xtermProcess(String message, Session session, OcTerminalSession ocTerminalSession) {
        DuplicateSessionMessage baseMessage = (DuplicateSessionMessage) getMessage(message);

        OcUser ocUser =  userFacade.getOcUserBySession();

        JSchSession jSchSession = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId(), baseMessage.getDuplicateInstanceId());

        assert jSchSession != null;
        String host = jSchSession.getHostSystem().getHost();
        boolean isAdmin = isOps(ocUser);
        HostSystem hostSystem = buildHostSystem(ocUser, host, baseMessage, isAdmin);

        RemoteInvokeHandler.openSSHTermOnSystem(ocTerminalSession.getSessionId(), baseMessage.getInstanceId(), hostSystem);
        terminalFacade.addOcTerminalSessionInstance(TerminalSessionInstanceBuilder.build(ocTerminalSession,hostSystem ,baseMessage.getDuplicateInstanceId()));
    }


    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, DuplicateSessionMessage.class);
    }

}
