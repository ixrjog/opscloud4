package com.baiyi.opscloud.sshcore.model;

import com.baiyi.opscloud.sshcore.message.KubernetesMessage;
import com.baiyi.opscloud.sshcore.task.kubernetes.WatchKubernetesTerminalOutputTask;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @Author baiyi
 * @Date 2021/7/15 1:55 下午
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KubernetesSession {

    private String sessionId;
    /**
     * 服务器唯一id
     * 会话复制后 id#uuid
     */
    private String instanceId;
    private PrintStream commander;
    private LogWatch logWatch;
    private ExecWatch execWatch;

    private WatchKubernetesTerminalOutputTask watchKubernetesTerminalOutputTask;

    private OutputStream inputToChannel;

    private static SessionOutput sessionOutput;

    public void resize(KubernetesMessage.Resize resizeMessage) {
        if (this.execWatch == null) {
            return;
        }
        execWatch.resize(resizeMessage.getCols(), resizeMessage.getRows());
    }

    public void setSessionOutput(SessionOutput sessionOutput) {
        KubernetesSession.sessionOutput = sessionOutput;
    }

    public SessionOutput getSessionOutput() {
        return KubernetesSession.sessionOutput;
    }

}