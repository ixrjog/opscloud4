package com.baiyi.opscloud.factory.xterm;

import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
public interface IXTermProcess {

    void xtermProcess(String message, Session session, OcTerminalSession ocTerminalSession);

    String getKey();

}
