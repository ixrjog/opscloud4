package com.baiyi.opscloud.xterm.model;

import com.jcraft.jsch.Channel;
import lombok.Data;

import java.io.OutputStream;
import java.io.PrintStream;

@Data
public class JSchSession {

    private String termSessionId;

    private String sessionId;
    /**
     * 服务器唯一id
     * 会话复制后 id#uuid
     */
    private String instanceId;
    private PrintStream commander;


    private OutputStream inputToChannel;
    private Channel channel;
    private HostSystem hostSystem;
    private static SessionOutput sessionOutput;


    public void setSessionOutput(SessionOutput sessionOutput) {
        JSchSession.sessionOutput = sessionOutput;
    }

    public SessionOutput getSessionOutput() {
        return JSchSession.sessionOutput;
    }

}
