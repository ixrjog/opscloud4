package com.baiyi.opscloud.sshcore.task.ssh;

import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.base.AbstractSshChannelOutputTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author baiyi
 * @Date 2021/7/21 6:24 下午
 * @Version 1.0
 */
@Slf4j
public class WatchKubernetesSshOutputTask extends AbstractSshChannelOutputTask {

    private OutputStream channelOutput;

    public WatchKubernetesSshOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream baos, OutputStream channelOutput) {
        setSessionOutput(sessionOutput);
        setBaos(baos);
        this.channelOutput = channelOutput;
    }

    @Override
    public void write(char[] buf, int off, int len) throws IOException {
        this.channelOutput.write(toBytes(buf), off, len);
       // System.out.write(toBytes(buf),off,len);
        this.channelOutput.flush();
    }

}

