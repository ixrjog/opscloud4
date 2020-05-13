package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.message.XTermInitialWSMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        XTermInitialWSMessage baseMessage = (XTermInitialWSMessage) getXTermMessage(message);
        String username = ocAuthFacade.getUserByToken(baseMessage.getToken());
        if (StringUtils.isEmpty(username)) return;
        OcUser ocUser = ocUserService.queryOcUserByUsername(username);

        BusinessWrapper wrapper = serverGroupFacade.getServerTreeHostPatternMap(baseMessage.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return;
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) wrapper.getBody();

        for (String instanceId : baseMessage.getInstanceIds()) {
            if (!serverTreeHostPatternMap.containsKey(instanceId))
                continue;
            String host = serverTreeHostPatternMap.get(instanceId);
            HostSystem hostSystem = buildHostSystem(ocUser, host, baseMessage);
            RemoteInvokeHandler.openSSHTermOnSystem(session.getId(), instanceId, hostSystem);
        }
    }




    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        XTermInitialWSMessage initialMessage = new GsonBuilder().create().fromJson(message, XTermInitialWSMessage.class);
        return initialMessage;
    }

}
