package com.baiyi.opscloud.sshcore.task.ssh;

import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.base.AbstractOutputTask;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;

import java.io.InputStream;

/**
 * @Author baiyi
 * @Date 2021/7/6 10:17 上午
 * @Version 1.0
 */
@Slf4j
public class WatchSshServerOutputTask extends AbstractOutputTask {

    Terminal terminal;

    public WatchSshServerOutputTask(SessionOutput sessionOutput, InputStream outFromChannel, Terminal terminal) {
        super(sessionOutput, outFromChannel);
        this.terminal = terminal;
    }

    @Override
    public void write(char[] buf, int off, int len) {
        terminal.writer().write(buf, off, len);
        terminal.writer().flush();
    }


}
