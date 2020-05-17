package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseXTermMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/13 6:50 下午
 * @Version 1.0 HEARTBEAT
 */
@Slf4j
@Component
public class XTermHeartbeatProcess extends BaseXTermProcess implements IXTermProcess {

    /**
     * XTerm心跳
     *
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.HEARTBEAT.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
       log.info("收到前端心跳");
    }

    @Override
    protected BaseXTermMessage getXTermMessage(String message) {
        return null;
    }

}
