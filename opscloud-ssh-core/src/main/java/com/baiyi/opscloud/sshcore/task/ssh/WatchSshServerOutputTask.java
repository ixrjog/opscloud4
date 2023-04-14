package com.baiyi.opscloud.sshcore.task.ssh;

import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.base.AbstractOutputTask;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author baiyi
 * @Date 2021/7/6 10:17 上午
 * @Version 1.0
 */
@Slf4j
public class WatchSshServerOutputTask extends AbstractOutputTask {

    private final OutputStream out;

    public WatchSshServerOutputTask(SessionOutput sessionOutput, InputStream outFromChannel, OutputStream out) {
        super(sessionOutput, outFromChannel);
        this.out = out;
    }

    @Override
    public void write(char[] buf, int off, int len) throws IOException {
        // fix bug
        this.write(buf);
    }

    private void write(char[] buf) throws IOException {
        out.write(toBytes(buf));
    }

}