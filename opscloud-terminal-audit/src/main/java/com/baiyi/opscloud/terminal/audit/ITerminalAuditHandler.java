package com.baiyi.opscloud.terminal.audit;

import jakarta.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:55 下午
 * @Version 1.0
 */
public interface ITerminalAuditHandler {

    /**
     *
     * @param message
     * @param session
     */
    void handle(String message, Session session);

    String getState();

}