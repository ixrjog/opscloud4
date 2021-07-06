package com.baiyi.opscloud.sshserver.command;

import com.baiyi.opscloud.common.exception.ssh.SshRuntimeException;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.sshcore.handler.HostSystemHandler;
import com.baiyi.opscloud.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.opscloud.sshcore.model.HostSystem;
import com.baiyi.opscloud.sshcore.model.JSchSession;
import com.baiyi.opscloud.sshcore.model.JSchSessionContainer;
import com.baiyi.opscloud.sshcore.task.ssh.SshSentOutputTask;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshContext;
import com.baiyi.opscloud.sshserver.SshShellCommandFactory;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.annotation.InvokeSessionUser;
import com.baiyi.opscloud.sshserver.command.component.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.util.SessionUtil;
import com.baiyi.opscloud.sshserver.util.TerminalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.session.ServerSession;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
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
@ShellCommandGroup("Server")
public class ServerLoginCommand {

    @Resource
    private SshShellHelper helper;

    @Resource
    private ServerService serverService;

    @Resource
    private HostSystemHandler hostSystemHandler;

    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "Login server", key = {"open", "login"})
    public void login(@ShellOption(help = "Server Id") int id, @ShellOption(help = "Account Name", defaultValue = "") String account) {
        ServerSession serverSession = helper.getSshSession();
        String sessionId = SessionUtil.buildSessionId(serverSession.getIoSession());
        String instanceId = IdUtil.buildUUID();
        Terminal terminal = getTerminal();
        SshSentOutputTask run = new SshSentOutputTask(sessionId, serverSession, terminal);
        Thread thread = new Thread(run);
        thread.start();
        try {
            HostSystem hostSystem;
            Map<Integer, Integer> idMapper = SessionCommandContext.getIdMapper();
            Server server = serverService.getById(idMapper.get(id));
            hostSystem = hostSystemHandler.buildHostSystem(server, account);
            hostSystem.setInstanceId(instanceId);
            hostSystem.setTerminalSize(helper.terminalSize());
            RemoteInvokeHandler.openSSHTermOnSystemForSSHServer(sessionId, hostSystem, terminal);
            terminal.enterRawMode();
            TerminalUtil.rawModeSupportVintr(terminal);
            Instant inst1 = Instant.now(); // 计时
            Size size = terminal.getSize();
            try {
                while (true) {
                    if (isClosed(sessionId, instanceId)) {
                        Thread.sleep(150L);
                        sessionClosed("用户正常退出登录! 耗时:%s/s", inst1);
                        break;
                    }
                    tryResize(size, terminal, sessionId, instanceId);
                    printJSchSession(sessionId, instanceId, terminal.reader().read(25L));
                    // Thread.sleep(25L); // 循环延迟补偿
                }
            } catch (Exception e) {
                e.printStackTrace();
                sessionClosed("服务端连接已断开! 耗时:%s/s", inst1);
            }
        } catch (SshRuntimeException e) {
            run.stop();
            throw e;
        }
        run.stop();
        JSchSessionContainer.closeSession(sessionId, instanceId);
    }

    private void tryResize(Size size, Terminal terminal, String sessionId, String instanceId) {
        if (!terminal.getSize().equals(size)) {
            size = terminal.getSize();
            TerminalUtil.resize(sessionId, instanceId, size);
        }
    }

    private Terminal getTerminal() {
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        if (sshContext == null) {
            throw new IllegalStateException("Unable to find ssh context");
        } else {
            return sshContext.getTerminal();
        }
    }

    private void sessionClosed(String logout, Instant inst1) {
        helper.print(String.format(logout, Duration.between(inst1, Instant.now()).getSeconds()), PromptColor.RED);
    }

    private boolean isClosed(String sessionId, String instanceId) {
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(sessionId, instanceId);
        assert jSchSession != null;
        return jSchSession.getChannel().isClosed();
    }

    private void printJSchSession(String sessionId, String instanceId, int ch) throws Exception {
        if (ch < 0) return;
        JSchSession jSchSession = JSchSessionContainer.getBySessionId(sessionId, instanceId);
        if (jSchSession == null) throw new Exception();
        jSchSession.getCommander().print((char) ch);
    }

}
