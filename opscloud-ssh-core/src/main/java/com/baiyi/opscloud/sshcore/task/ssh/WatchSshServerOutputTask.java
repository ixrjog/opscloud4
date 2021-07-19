package com.baiyi.opscloud.sshcore.task.ssh;

import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.task.base.AbstractOutputTask;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
    public void write(char[] buf, int off, int len) throws IOException {
       // terminal.writer().write(buf, off, len);
      //  terminal.flush();
        System.out.print(buf);
        System.out.println("off = " + off + "; len = " + len);
        terminal.output().write(getBytes(buf), off, len);
        terminal.output().flush();
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = StandardCharsets.UTF_8;
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }
}
