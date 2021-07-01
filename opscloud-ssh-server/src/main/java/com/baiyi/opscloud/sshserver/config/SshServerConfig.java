package com.baiyi.opscloud.sshserver.config;

import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.listeners.SshShellListener;
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

//    private Terminal terminal;
//
//    @Autowired
//    @Lazy
//    public void setTerminal(Terminal terminal) {
//        this.terminal = terminal;
//    }

    @Bean
    public SshShellListener sshShellListener() {
        return event -> {
            // 清屏
            // this.terminal.puts(InfoCmp.Capability.clear_screen, new Object[0]);
            String welcome = String.format("%s 欢迎使用 Opscloud SSH Server! \n", event.getSession().getServerSession().getUsername());
            helper.print(welcome, PromptColor.RED);
        };
    }
}
