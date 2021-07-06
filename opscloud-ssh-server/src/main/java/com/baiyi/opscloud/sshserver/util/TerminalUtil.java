package com.baiyi.opscloud.sshserver.util;

import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.jcraft.jsch.ChannelShell;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

/**
 * @Author baiyi
 * @Date 2021/6/7 9:55 上午
 * @Version 1.0
 */
public class TerminalUtil {

    private TerminalUtil() {
    }

    // 行模式支持 Ctrl+C
    public static void rawModeSupportVintr(Terminal terminal) {
        Attributes prvAttr = terminal.getAttributes();
        Attributes newAttr = new Attributes(prvAttr);
        newAttr.setControlChar(Attributes.ControlChar.VINTR, 0);
        terminal.setAttributes(newAttr);
    }

    public static void resize(String sessionId, String instanceId, Size size) {
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(sessionId, instanceId);
        RemoteInvokeHandler.setChannelPtySize((ChannelShell) jSchSession.getChannel(), size);
    }
}
