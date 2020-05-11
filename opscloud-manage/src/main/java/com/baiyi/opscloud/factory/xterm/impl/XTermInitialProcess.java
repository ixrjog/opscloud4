package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.handler.ConnectionSession;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.message.XTermInitialWSMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:36 上午
 * @Version 1.0
 */
@Component
public class XTermInitialProcess extends BaseXTermProcess implements IXTermProcess {

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
            SSHKeyCredential sshKeyCredential = keyboxFacade.getSSHKeyCredential("admin");

            HostSystem hostSystem = new HostSystem();
            hostSystem.setHost(host);
            hostSystem.setSshKeyCredential(sshKeyCredential);
            hostSystem.setInitialMessage(initialMessage);

            ConnectionSession connectionSession =  RemoteInvokeHandler.getSession( instanceId, hostSystem);
//            Runnable run = new SentOutputTask(sessionId, session);
//            Thread thread = new Thread(run);
//            thread.start();

            if (connectionSession != null) {
                connectionSessionMap.put(session.getId(), connectionSession);

                //WSResult loginSuccess = new WSResult(socketContext.getId(), "", WSContentType.loginSuccss.getCode());
                //doWriteToSession(session, loginSuccess);
                connectionSession.registerRead(socketContext.getId(), new SessionBridge() {
                    @Override
                    public void process(String value) {
                        try {
                            WSResult WSResult = new WSResult(socketContext.getId(), value, com.sdg.cmdb.domain.keybox.WSContentType.response.getCode());
                            doWriteToSession(session, WSResult);
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                });
            } else {
                logger.warn(socketContext.getId() + " do ssh connection for " + hostSystem.getHost() + " error with:" + hostSystem.getErrorMsg());

                WSResult WSResult = new WSResult(socketContext.getId(), hostSystem.getErrorMsg(), WSContentType.loginFailure.getCode());
                doWriteToSession(session, WSResult);
            }

        }
    }

    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        XTermInitialWSMessage initialMessage = new GsonBuilder().create().fromJson(message, XTermInitialWSMessage.class);
        return initialMessage;
    }

}
