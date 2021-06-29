package com.baiyi.caesar.sshserver.command;

import com.baiyi.caesar.common.exception.ssh.SshRuntimeException;
import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.sshcore.handler.HostSystemHandler;
import com.baiyi.caesar.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.caesar.sshcore.model.HostSystem;
import com.baiyi.caesar.sshcore.model.JSchSession;
import com.baiyi.caesar.sshcore.model.JSchSessionContainer;
import com.baiyi.caesar.sshcore.task.ssh.SshSentOutputTask;
import com.baiyi.caesar.sshserver.PromptColor;
import com.baiyi.caesar.sshserver.SshContext;
import com.baiyi.caesar.sshserver.SshShellCommandFactory;
import com.baiyi.caesar.sshserver.SshShellHelper;
import com.baiyi.caesar.sshserver.annotation.InvokeSessionUser;
import com.baiyi.caesar.sshserver.util.SessionUtil;
import com.baiyi.caesar.sshserver.util.TerminalUtil;
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

/**
 * @Author baiyi
 * @Date 2021/5/31 4:58 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Server")
public class LoginCommand {

    @Resource
    private SshShellHelper helper;

    @Resource
    private ServerService serverService;

    @Resource
    private HostSystemHandler hostSystemHandler;

    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "Login server", key = {"open", "login"})
    public void login(@ShellOption(help = "Server id") int id, @ShellOption(help = "Account name", defaultValue = "") String account) {
        ServerSession serverSession = helper.getSshSession();
        String sessionId = SessionUtil.buildSessionId(serverSession.getIoSession());
        String instanceId = IdUtil.buildUUID();
        Terminal terminal = getTerminal();
        SshSentOutputTask run = new SshSentOutputTask(sessionId, serverSession, terminal);
        Thread thread = new Thread(run);
        thread.start();
        try {
            HostSystem hostSystem;
            Server server = serverService.getById(id);
            hostSystem = hostSystemHandler.buildHostSystem(server, account);
            hostSystem.setInstanceId(instanceId);
            hostSystem.setTerminalSize(helper.terminalSize());
            RemoteInvokeHandler.openSSHTermOnSystemForSSHServer(sessionId, hostSystem);
            TerminalUtil.enterRawMode(terminal);
            Instant inst1 = Instant.now(); // 计时
            Size size = terminal.getSize();
            while (true) {
                try {
                    if (isClosed(sessionId, instanceId)) {
                        Thread.sleep(150L);
                        sessionClosed("用户正常退出登录! 耗时:%s/s", inst1);
                        break;
                    }
                    tryResize(size, terminal, sessionId, instanceId);
                    printJSchSession(sessionId, instanceId, terminal.reader().read(25L));
                    //Thread.sleep(25L); // 循环延迟补偿
                } catch (Exception e) {
                    e.printStackTrace();
                    sessionClosed("服务端连接已断开! 耗时:%s/s", inst1);
                    break;
                }
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
