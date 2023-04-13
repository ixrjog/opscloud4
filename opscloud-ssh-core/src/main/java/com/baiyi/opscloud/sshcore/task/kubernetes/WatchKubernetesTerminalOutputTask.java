package com.baiyi.opscloud.sshcore.task.kubernetes;

import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.base.AbstractSshChannelOutputTask;
import com.baiyi.opscloud.sshcore.util.SessionOutputUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * @Author baiyi
 * @Date 2021/7/16 10:42 上午
 * @Version 1.0
 */
@Slf4j
public class WatchKubernetesTerminalOutputTask extends AbstractSshChannelOutputTask {

    public WatchKubernetesTerminalOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream baos) {
        setSessionOutput(sessionOutput);
        setOutputStream(baos);
    }

    @Override
    public void write(char[] buf, int off, int len) {
        SessionOutputUtil.addToOutput(getSessionOutput().getSessionId(), getSessionOutput().getInstanceId(), buf, off, len);
    }

}