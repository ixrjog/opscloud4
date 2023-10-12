package com.baiyi.opscloud.sshserver.command.custom.server;

import com.baiyi.opscloud.common.exception.ssh.SshException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.interceptor.SuperAdminInterceptor;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.sshcore.audit.ServerCommandAudit;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshcore.handler.HostSystemHandler;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.model.HostSystem;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.sshcore.model.SessionIdMapper;
import com.baiyi.opscloud.sshserver.*;
import com.baiyi.opscloud.sshserver.aop.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.aop.annotation.SettingContextSessionUser;
import com.baiyi.opscloud.sshserver.command.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.custom.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.packer.SshServerPacker;
import com.baiyi.opscloud.sshserver.util.TerminalUtil;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.channel.ChannelOutputStream;
import org.apache.sshd.server.session.ServerSession;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/31 4:58 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Server Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + BaseServerCommand.GROUP + ".create", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class ServerLoginCommand extends BaseServerCommand {

    private final ServerCommandAudit serverCommandAudit;

    private final SshShellHelper sshShellHelper;

    private final ServerService serverService;

    private final HostSystemHandler hostSystemHandler;

    private final SshServerPacker sshServerPacker;

    private final SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    private final SuperAdminInterceptor sAInterceptor;

    private String toInstanceId(Server server) {
        ServerVO.Server serverVO = BeanCopierUtil.copyProperties(server, ServerVO.Server.class);
        sshServerPacker.wrap(serverVO);
        return Joiner.on("#").join(serverVO.getDisplayName(), server.getPrivateIp(), IdUtil.buildUUID());
    }

    @SuppressWarnings("SpringShellCommandInspection")
    @ScreenClear
    @SettingContextSessionUser(invokeAdmin = true)
    @ShellMethod(key = {COMMAND_SERVER_LOGIN, "login", "open"}, value = "Login to the server.")
    public void login(@ShellOption(help = "ID", defaultValue = "1") int id,
                      @ShellOption(help = "Account", defaultValue = "") String account,
                      @ShellOption(value = {"-A", "--admin"}, help = "Admin") boolean admin) {
        ServerSession serverSession = sshShellHelper.getSshSession();
        String sessionId = SessionIdMapper.getSessionId(serverSession.getIoSession());
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        ChannelOutputStream out = (ChannelOutputStream) sshContext.getSshShellRunnable().getOs();
        Map<Integer, Integer> idMapper = SessionCommandContext.getIdMapper();
        Server server = serverService.getById(idMapper.get(id));
        sAInterceptor.interceptLoginServer(server.getId());
        String instanceId = toInstanceId(server);
        try {
            HostSystem hostSystem = hostSystemHandler.buildHostSystem(server, account, admin);
            hostSystem.setInstanceId(instanceId);
            hostSystem.setTerminalSize(sshShellHelper.terminalSize());
            TerminalSessionInstance terminalSessionInstance = TerminalSessionInstanceBuilder.build(sessionId, hostSystem, InstanceSessionTypeEnum.SERVER);
            simpleTerminalSessionFacade.recordTerminalSessionInstance(terminalSessionInstance);
            RemoteInvokeHandler.openSSHServer(sessionId, hostSystem, out);
            TerminalUtil.enterRawMode(terminal);
            // 无延迟
            out.setNoDelay(true);
            Size size = terminal.getSize();
            try {
                while (true) {
                    if (isClosed(sessionId, instanceId)) {
                        NewTimeUtil.millisecondsSleep(150L);
                        break;
                    }
                    doResize(size, terminal, sessionId, instanceId);
                    int input = terminal.reader().read(5L);
                    send(sessionId, instanceId, input);
                }
            } catch (Exception e) {
                // printLogout("Server connection disconnected, session duration %s/s", inst1);
            } finally {
                simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSessionInstance);
            }
        } catch (SshException e) {
            String msg = StringFormatter.format("SSH connection error: {}", e.getMessage());
            log.error(msg);
            sshShellHelper.print(msg, PromptColor.RED);
        } finally {
            serverCommandAudit.asyncRecordCommand(sessionId, instanceId);
            JSchSessionContainer.closeSession(sessionId, instanceId);
        }
    }

    private void doResize(Size size, Terminal terminal, String sessionId, String instanceId) {
        if (!terminal.getSize().equals(size)) {
            size = terminal.getSize();
            TerminalUtil.resize(sessionId, instanceId, size);
        }
    }

    /**
     * 打印会话关闭信息
     *
     * @param logout
     * @param instant
     */
    private void printLogout(String logout, Instant instant) {
        sshShellHelper.print(String.format(logout, Duration.between(instant, Instant.now()).getSeconds()), PromptColor.RED);
    }

    private boolean isClosed(String sessionId, String instanceId) {
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(sessionId, instanceId);
        assert jSchSession != null;
        return jSchSession.getChannel().isClosed();
    }

    private void send(String sessionId, String instanceId, int input) throws Exception {
        if (input < 0) {
            return;
        }
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(sessionId, instanceId);
        if (jSchSession == null) {
            throw new Exception();
        }
        char ch = (char) input;
        jSchSession.getCommander().print(ch);
    }

}