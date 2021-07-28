package com.baiyi.opscloud.terminal.audit;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:55 下午
 * @Version 1.0
 */
public interface ITerminalAuditProcess {

    /**
     *
     * @param message
     * @param session
     */
    void process(String message, Session session);

    String getState();
}
