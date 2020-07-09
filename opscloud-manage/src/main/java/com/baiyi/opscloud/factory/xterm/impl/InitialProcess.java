package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import com.baiyi.opscloud.xterm.message.InitialMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:36 上午
 * @Version 1.0
 */
@Component
public class InitialProcess extends BaseProcess implements IXTermProcess {


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
    public void xtermProcess(String message, Session session, OcTerminalSession ocTerminalSession) {
        InitialMessage xtermMessage = (InitialMessage) getMessage(message);
        OcUser ocUser = userFacade.getOcUserBySession();
        BusinessWrapper<Map<String, String>> wrapper = serverGroupFacade.getServerTreeHostPatternMap(xtermMessage.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return;
        Map<String, String> serverTreeHostPatternMap = wrapper.getBody();
        boolean isAdmin = isOps(ocUser);
        heartbeat(ocTerminalSession.getSessionId());

        xtermMessage.getInstanceIds().forEach(k -> {
            if (serverTreeHostPatternMap.containsKey(k)) {
                String host = serverTreeHostPatternMap.get(k);
                HostSystem hostSystem = buildHostSystem(ocUser, host, xtermMessage, isAdmin);
                RemoteInvokeHandler.openSSHTermOnSystem(ocTerminalSession.getSessionId(), k, hostSystem);
                terminalFacade.addOcTerminalSessionInstance(TerminalSessionInstanceBuilder.build(ocTerminalSession, hostSystem));
            }
        });
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, InitialMessage.class);
    }

}
