package com.baiyi.opscloud.sshcore.model;

import com.jcraft.jsch.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @Author baiyi
 * @Date 2021/6/2 6:36 下午
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SshSession {


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
        SshSession.sessionOutput = sessionOutput;
    }

    public SessionOutput getSessionOutput() {
        return SshSession.sessionOutput;
    }

}