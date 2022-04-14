package com.baiyi.opscloud.sshserver.command.server;

import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.sshserver.annotation.CheckTerminalSize;
import com.baiyi.opscloud.sshserver.annotation.InvokeSessionUser;
import com.baiyi.opscloud.sshserver.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.annotation.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.context.ListServerCommand;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.server.base.BaseServerCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * @Author baiyi
 * @Date 2021/6/7 11:47 上午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Server")
public class ServerCommand extends BaseServerCommand {

    @CheckTerminalSize(cols = 130,rows = 10)
    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "查询授权服务器列表信息", key = {"ls", "list"})
    public void listServer(@ShellOption(help = "Server", defaultValue = "") String name, @ShellOption(help = "IP", defaultValue = "") String ip) {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = ServerParam.UserPermissionServerPageQuery.builder()
                .name(name)
                .queryIp(ip)
                .page(1)
                .build();
        ListServerCommand commandContext = ListServerCommand.builder()
                .sessionId(sessionId)
                .username(sshShellHelper.getSshSession().getUsername())
                .queryParam(pageQuery)
                .build();
        doListServer(commandContext);
    }

    @CheckTerminalSize(cols = 130,rows = 10)
    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "查询授权服务器列表信息（上一页）", key = "b")
    public void beforePage() {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = SessionCommandContext.getServerQuery();
        if (pageQuery != null) {
            pageQuery.setPage(pageQuery.getPage() > 1 ? pageQuery.getPage() - 1 : pageQuery.getPage());
            ListServerCommand listServerCommand = ListServerCommand.builder()
                    .sessionId(sessionId)
                    .username(sshShellHelper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            doListServer(listServerCommand);
        } else {
            listServer("", "");
        }
    }

    @CheckTerminalSize(cols = 130,rows = 10)
    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "查询授权服务器列表信息（下一页）", key = "n")
    public void nextPage() {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = SessionCommandContext.getServerQuery();
        if (pageQuery != null) {
            pageQuery.setPage(pageQuery.getPage() + 1);
            ListServerCommand listServerCommand = ListServerCommand.builder()
                    .sessionId(sessionId)
                    .username(sshShellHelper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            doListServer(listServerCommand);
        } else {
            listServer("", "");
        }
    }

}
