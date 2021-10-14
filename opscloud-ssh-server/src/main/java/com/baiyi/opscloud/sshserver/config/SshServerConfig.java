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

    private void xterm256Color() {
        StringBuilder sb = new StringBuilder();
        sb.append(getColorStrs(16, 21)).append(getColorStrs(22, 27)).append(getColorStrs(28, 33)).append("\n")
                .append(getColorStrs(52, 57)).append(getColorStrs(58, 63)).append(getColorStrs(64, 69)).append("\n")
                .append(getColorStrs(88, 93)).append(getColorStrs(94, 99)).append(getColorStrs(100, 105)).append("\n")
                .append(getColorStrs(124, 129)).append(getColorStrs(130, 135)).append(getColorStrs(136, 141)).append("\n")
                .append(getColorStrs(160, 165)).append(getColorStrs(166, 171)).append(getColorStrs(172, 177)).append("\n")
                .append(getColorStrs(196, 201)).append(getColorStrs(202, 207)).append(getColorStrs(208, 213)).append("\n\n")

                .append(getColorStrs(34, 39)).append(getColorStrs(40, 45)).append(getColorStrs(46, 51)).append("\n")
                .append(getColorStrs(70, 75)).append(getColorStrs(76, 81)).append(getColorStrs(82, 87)).append("\n")
                .append(getColorStrs(106, 111)).append(getColorStrs(112, 117)).append(getColorStrs(118, 123)).append("\n")
                .append(getColorStrs(142, 147)).append(getColorStrs(148, 153)).append(getColorStrs(154, 159)).append("\n")
                .append(getColorStrs(178, 183)).append(getColorStrs(184, 189)).append(getColorStrs(190, 195)).append("\n")
                .append(getColorStrs(214, 219)).append(getColorStrs(220, 225)).append(getColorStrs(226, 231)).append("\n\n")

                .append(getColorStrs(232, 239)).append("\n")
                .append(getColorStrs(240, 247)).append("\n")
                .append(getColorStrs(248, 255)).append("\n");
        helper.print(sb.toString());
    }

    private String getColorStrs(int start, int end) {
        StringBuilder sb = new StringBuilder();
        do {

            sb.append(helper.getColoredMessage(String.valueOf(start), start));
            start++;
        } while (start <= end);
        return sb.toString() + "\t";
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
