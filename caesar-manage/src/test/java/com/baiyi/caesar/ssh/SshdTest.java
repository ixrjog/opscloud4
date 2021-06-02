package com.baiyi.caesar.ssh;

import com.baiyi.caesar.BaseUnit;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.shell.ShellFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/6/2 10:10 上午
 * @Version 1.0
 */
public class SshdTest extends BaseUnit {

    @Test
    void ServerTest() {
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setHost("127.0.0.1");
        sshd.setPort(22222);
        ShellFactory shellFactory = new ProcessShellFactory("/bin/sh", "-i", "-l");
        sshd.setShellFactory(shellFactory);
        try {
            sshd.start();
        }catch (IOException e){

        }


    }
}
