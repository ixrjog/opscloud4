package com.baiyi.opscloud.sshserver.config;

import com.baiyi.opscloud.common.model.HostInfo;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionBuilder;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshcore.model.SessionIdMapper;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;
import com.baiyi.opscloud.sshserver.listeners.SshShellEventType;
import com.baiyi.opscloud.sshserver.listeners.SshShellListener;
import org.apache.sshd.common.session.SessionContext;
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

//    @Resource
//    @Lazy
//    private EventCommand eventCommand;

    private final static HostInfo serverInfo = HostInfo.build();

    @Resource
    private SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    private static final String WELCOME = "%s 欢迎使用 Opscloud SSH Server! \n";

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
            SshShellEventType eventType = event.getType();
            // 登录事件
            // this.terminal.puts(InfoCmp.Capability.clear_screen, new Object[0]);  // 清屏
            if (SshShellEventType.SESSION_STARTED.toString().equals(eventType.toString())) {
                recordTerminalSession(event);
                String welcome = String.format(WELCOME, event.getSession().getServerSession().getUsername());
                helper.print(welcome, PromptColor.RED);
                // 展示事件
                // eventCommand.listEvent("");
                return;
            }
            // 关闭事件
            if (SshShellEventType.SESSION_STOPPED.toString().equals(eventType.toString())
                    || SshShellEventType.SESSION_STOPPED_UNEXPECTEDLY.toString().equals(eventType.toString())) {
                closeTerminalSession(event);
            }
        };
    }

    private void recordTerminalSession(SshShellEvent event) {
        //  SessionIdMapper.put(event.getSession().getServerSession().getIoSession());
        String sessionId = SessionIdMapper.getSessionId(event.getSession().getServerSession().getIoSession());
        SessionContext sc = event.getSession().getSessionContext();
        TerminalSession terminalSession = TerminalSessionBuilder.build(sessionId, event.getSession().getServerSession().getUsername(), serverInfo, sc.getRemoteAddress(), SessionTypeEnum.SSH_SERVER);
        simpleTerminalSessionFacade.recordTerminalSession(terminalSession);
    }

    private void closeTerminalSession(SshShellEvent event) {
        String sessionId = SessionIdMapper.getSessionId(event.getSession().getServerSession().getIoSession());
        TerminalSession terminalSession = simpleTerminalSessionFacade.getTerminalSessionBySessionId(sessionId);
        simpleTerminalSessionFacade.closeTerminalSession(terminalSession);
    }

}
