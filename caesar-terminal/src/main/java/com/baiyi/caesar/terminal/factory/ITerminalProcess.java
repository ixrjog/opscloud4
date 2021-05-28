package com.baiyi.caesar.terminal.factory;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
public interface ITerminalProcess {

    void process(String message, Session session, TerminalSession terminalSession);

    String getState();

}
