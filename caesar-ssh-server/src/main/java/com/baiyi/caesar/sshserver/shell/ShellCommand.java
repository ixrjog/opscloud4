package com.baiyi.caesar.sshserver.shell;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.service.server.ServerGroupService;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.sshcore.handler.HostSystemHandler;
import com.baiyi.caesar.sshcore.handler.RemoteInvokeHandler;
import com.baiyi.caesar.sshcore.model.HostSystem;
import com.baiyi.caesar.sshcore.model.JSchSession;
import com.baiyi.caesar.sshcore.model.JSchSessionMap;
import com.baiyi.caesar.sshcore.task.server.ServerSentOutputTask;
import com.github.fonimus.ssh.shell.SimpleTable;
import com.github.fonimus.ssh.shell.SshContext;
import com.github.fonimus.ssh.shell.SshShellCommandFactory;
import com.github.fonimus.ssh.shell.SshShellHelper;
import com.github.fonimus.ssh.shell.commands.SshShellComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.session.ServerSession;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/31 4:58 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
public class ShellCommand {

    @Resource
    private SshShellHelper helper;

    @Resource
    private ServerService serverService;

    @Resource
    private HostSystemHandler hostSystemHandler;

    @Resource
    private ServerGroupService serverGroupService;

    @ShellMethod("Show ServerGroup")
    public String show() {
        String name = helper.read("Select serverGroup ?");
        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("ID|")
                .column("Server Group |")
                .column("Comment ")
                .useFullBorder(false);
        ServerGroupParam.ServerGroupPageQuery pageQuery = ServerGroupParam.ServerGroupPageQuery.builder()
                .name(name)
                .build();
        pageQuery.setLength(100);
        pageQuery.setPage(1);
        List<ServerGroup> groups = serverGroupService.queryServerGroupPage(pageQuery).getData();
        groups.forEach(s ->
                builder.line(Arrays.asList(" " + s.getId() + " |", s.getName(), s.getComment()))
        );
        // SshContext sshContext = (SshContext) SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        //             System.err.println(JSON.toJSON(sshContext));

        String result = helper.renderTable(builder.build());
        result += "\n 页码：2，每页行数：42，总页数：14，总数量：559\n";

        return result;
    }

    @ShellMethod("Show ServerGroup")
    public String select() {
        ServerSession session = helper.getSshSession();

        System.out.println(JSON.toJSON(session));

        String name = helper.read("Select serverGroup ?");

        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("ID ")
                .column("Server Name ")
                .column("IP ")
                .column("Comment ")
                .useFullBorder(true);
        List<Server> servers = serverService.queryByServerGroupId(Integer.parseInt(name));
        servers.forEach(server ->
                builder.line(Arrays.asList(server.getId(), server.getName(), server.getPrivateIp(), server.getComment())).clearLineAligners()
        );
        return helper.renderTable(builder.build());
    }

    @ShellMethod(value = "登录服务器", key = "login")
    public void login(@ShellOption(help = "输入服务器id") int id) {

        ServerSession serverSession = helper.getSshSession();
        String sessionId = String.valueOf(serverSession.getSessionId());
        // 线程启动
        Terminal terminal = getTerminal();
        Runnable run = new ServerSentOutputTask(sessionId, serverSession, terminal);
        Thread thread = new Thread(run);
        thread.start();

        Server server = serverService.getById(id);

        HostSystem hostSystem = hostSystemHandler.buildHostSystem(server);
        String instanceId = IdUtil.buildUUID();
        hostSystem.setInstanceId(instanceId);
        hostSystem.setTerminalSize(helper.terminalSize());
        RemoteInvokeHandler.openSSHTermOnSystemForSSHServer(sessionId, hostSystem);
        enterRawMode(terminal);
        while (true) {
            try {
                int ch = terminal.reader().read(25L);
                if (ch >= 0) {
                    printCommand(sessionId, instanceId, (char) ch);
                }
                // Thread.sleep(25L);
            } catch (Exception e) {
                e.printStackTrace();
                helper.print("连接已断开！");
                break;
            }
        }
    }

    // 原始模式 Ctrl+C
    private void enterRawMode(Terminal terminal) {
        Attributes prvAttr = terminal.getAttributes();
        Attributes newAttr = new Attributes(prvAttr);
        newAttr.setLocalFlags(EnumSet.of(Attributes.LocalFlag.ICANON, Attributes.LocalFlag.ECHO, Attributes.LocalFlag.IEXTEN), false);
        newAttr.setInputFlags(EnumSet.of(Attributes.InputFlag.IXON, Attributes.InputFlag.ICRNL, Attributes.InputFlag.INLCR), false);
        newAttr.setControlChar(Attributes.ControlChar.VMIN, 1);
        newAttr.setControlChar(Attributes.ControlChar.VTIME, 0);
        newAttr.setControlChar(Attributes.ControlChar.VINTR, 0);
        terminal.setAttributes(newAttr);
    }


    private Terminal getTerminal() {
        SshContext sshContext = (SshContext) SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        if (sshContext == null) {
            throw new IllegalStateException("Unable to find ssh context");
        } else {
            return sshContext.getTerminal();
        }
    }

    private void printCommand(String sessionId, String instanceId, char cmd) throws Exception  {
        JSchSession jSchSession = JSchSessionMap.getBySessionId(sessionId, instanceId);
        if (jSchSession == null) throw new Exception();
        jSchSession.getCommander().print(cmd);
    }


//    PublickeyAuthenticator
//    @Bean
//    public SshShellAuthenticationProvider sshShellAuthenticationProvider() {
//        return (user, pass, serverSession) -> user.equals(pass);
//    }


}
