package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.message.XTermDuplicateSessionWSMessage;
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
        XTermDuplicateSessionWSMessage baseMessage = (XTermDuplicateSessionWSMessage) getXTermMessage(message);
        String username = ocAuthFacade.getUserByToken(baseMessage.getToken());
        if (StringUtils.isEmpty(username)) return;
        OcUser ocUser = ocUserService.queryOcUserByUsername(username);

        JSchSession jSchSession = JSchSessionMap.getBySessionId(session.getId(), baseMessage.getDuplicateInstanceId());

        String host = jSchSession.getHostSystem().getHost();
        HostSystem hostSystem = buildHostSystem(ocUser, host, baseMessage);

        RemoteInvokeHandler.getSession(session.getId(), baseMessage.getInstanceId(), hostSystem);
    }


    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        XTermDuplicateSessionWSMessage baseMessage = new GsonBuilder().create().fromJson(message, XTermDuplicateSessionWSMessage.class);
        return baseMessage;
    }

}
