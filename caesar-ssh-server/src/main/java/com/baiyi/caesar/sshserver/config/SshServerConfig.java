package com.baiyi.caesar.sshserver.config;

import com.github.fonimus.ssh.shell.PromptColor;
import com.github.fonimus.ssh.shell.SshShellHelper;
import com.github.fonimus.ssh.shell.listeners.SshShellListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/9 4:50 下午
 * @Version 1.0
 */
@Configuration
public class SshServerConfig {

    @Resource
    private SshShellHelper helper;

    @Bean
    public SshShellListener sshShellListener() {
        return event -> {
            String welcome = String.format("%s 欢迎使用 Caesar SSH Server! \n", event.getSession().getServerSession().getUsername());
            helper.print(welcome, PromptColor.RED);
        };
    }
}
