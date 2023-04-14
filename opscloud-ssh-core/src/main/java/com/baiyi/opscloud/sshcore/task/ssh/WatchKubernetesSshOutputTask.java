package com.baiyi.opscloud.sshcore.task.ssh;

import cn.hutool.core.util.ArrayUtil;
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

//    private PrintWriter printWriter;
//
//    public WatchKubernetesSshOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream byteArrayOutputStream, PrintWriter printWriter) {
//        setSessionOutput(sessionOutput);
//        setOutputStream(byteArrayOutputStream);
//        this.printWriter = printWriter;
//    }

    public WatchKubernetesSshOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream baos, OutputStream channelOutput) {
        setSessionOutput(sessionOutput);
        setOutputStream(baos);
        this.channelOutput = channelOutput;
    }

    @Override
    public void write(char[] buff, int off, int len) throws IOException {
//        if (printWriter != null) {
//            this.printWriter.write(buff, off, len);
//            this.printWriter.flush();
//            return;
//        }
        char[] outBuff = ArrayUtil.sub(buff, 0, len);
        this.channelOutput.write(toBytes(outBuff));
    }

}

