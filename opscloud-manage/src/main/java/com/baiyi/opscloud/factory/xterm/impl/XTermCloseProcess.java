package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Component
public class XTermCloseProcess extends BaseXTermProcess implements IXTermProcess {

    @Override
    public String getKey() {
        return XTermRequestStatus.CLOSE.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        // XTermCloseWSMessage closeMessage = (XTermCloseWSMessage) getXTermMessage(message);
        try {
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        return null;
    }
}
