package com.baiyi.opscloud.sshserver.util;

import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.jcraft.jsch.ChannelShell;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import java.util.EnumSet;

/**
 * @Author baiyi
 * @Date 2021/6/7 9:55 上午
 * @Version 1.0
 */
public class TerminalUtil {

    private TerminalUtil() {
    }

    /**
     * 行模式支持 Ctrl+C
     * https://ftp.gnu.org/old-gnu/Manuals/glibc-2.2.3/html_node/libc_355.html
     *
     * @param terminal
     */
    public static void enterRawMode(Terminal terminal) {
        Attributes prvAttr = terminal.getAttributes();
        Attributes newAttr = new Attributes(prvAttr);
        // adopted from org.jline.builtins.Nano
        // see also https://en.wikibooks.org/wiki/Serial_Programming/termios#Basic_Configuration_of_a_Serial_Interface
        // no line processing
        // canonical mode off, echo off, echo newline off, extended input processing off / 规范模式关闭，回显关闭，回显换行关闭，扩展输入处理关闭
        newAttr.setLocalFlags(EnumSet.of(Attributes.LocalFlag.ICANON, Attributes.LocalFlag.ECHO, Attributes.LocalFlag.IEXTEN), false);
        // turn off input processing / 关闭输入处理
        newAttr.setInputFlags(EnumSet.of(Attributes.InputFlag.IXON, Attributes.InputFlag.ICRNL, Attributes.InputFlag.INLCR), false);

        ///////
        // newAttr.setLocalFlags(EnumSet.of(Attributes.LocalFlag.ISIG), true);

        // one input byte is enough to return from read, inter-character timer off
        // VMIN 1 / VTIME 0 / VINTR 0
        newAttr.setControlChar(Attributes.ControlChar.VMIN, 1);
        newAttr.setControlChar(Attributes.ControlChar.VTIME, 0);
        newAttr.setControlChar(Attributes.ControlChar.VINTR, 0);
        terminal.setAttributes(newAttr);
        // terminal.puts(InfoCmp.Capability.enter_ca_mode);
    }

    private static int getFlag(Attributes attributes, Attributes.InputFlag flag) {
        return attributes.getInputFlag(flag) ? 1 : 0;
    }

    private static int getFlag(Attributes attributes, Attributes.OutputFlag flag) {
        return attributes.getOutputFlag(flag) ? 1 : 0;
    }

    private static int getFlag(Attributes attributes, Attributes.LocalFlag flag) {
        return attributes.getLocalFlag(flag) ? 1 : 0;
    }

    public static void resize(String sessionId, String instanceId, Size size) {
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(sessionId, instanceId);
        assert jSchSession != null;
        RemoteInvokeHandler.setChannelPtySize((ChannelShell) jSchSession.getChannel(), size);
    }

}
