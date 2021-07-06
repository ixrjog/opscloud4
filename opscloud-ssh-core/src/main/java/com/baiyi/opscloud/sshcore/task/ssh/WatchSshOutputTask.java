package com.baiyi.opscloud.sshcore.task.ssh;

import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.util.SessionOutputUtil;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author baiyi
 * @Date 2021/7/6 10:17 上午
 * @Version 1.0
 */
@Slf4j
public class WatchSshOutputTask implements Runnable {

    InputStream outFromChannel;
    SessionOutput sessionOutput;
    Terminal terminal;

    public WatchSshOutputTask(SessionOutput sessionOutput, InputStream outFromChannel, Terminal terminal) {
        this.sessionOutput = sessionOutput;
        this.outFromChannel = outFromChannel;
        this.terminal = terminal;
    }

    @Override
    public void run() {
        InputStreamReader isr = new InputStreamReader(outFromChannel);
        BufferedReader br = new BufferedReader(isr);
        try {
            SessionOutputUtil.addOutput(sessionOutput);
            char[] buff = new char[32768];
            int read;
            while ((read = br.read(buff)) != -1) {
                log.info("read = {}", read);
                terminal.writer().write( buff, 0, read);
                terminal.writer().flush();
                //  SessionOutputUtil.addToOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId(), buff, 0, read);
                Thread.sleep(50);
            }
            SessionOutputUtil.removeOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId());
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
    }

}
