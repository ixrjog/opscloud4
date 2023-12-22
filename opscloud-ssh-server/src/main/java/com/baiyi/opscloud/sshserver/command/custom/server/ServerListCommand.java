package com.baiyi.opscloud.sshserver.command.custom.server;


import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.aop.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.aop.annotation.SettingContextSessionUser;
import com.baiyi.opscloud.sshserver.command.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.custom.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.custom.server.param.QueryServerParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * @Author baiyi
 * @Date 2023/4/4 13:12
 * @Version 1.0
 */
@SshShellComponent
@ShellCommandGroup("Server Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + BaseServerCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class ServerListCommand extends BaseServerCommand {

    @SuppressWarnings("SpringShellCommandInspection")
    @ScreenClear
    @SettingContextSessionUser(invokeAdmin = true)
    @ShellMethod(key = {COMMAND_SERVER_LIST, "ls"}, value = "List server info.")
    public void list(@ShellOption(help = "Server", defaultValue = "") String name, @ShellOption(help = "IP", defaultValue = "") String ip) {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = ServerParam.UserPermissionServerPageQuery.builder()
                .name(name)
                .queryIp(ip)
                .page(1)
                .build();
        QueryServerParam param = QueryServerParam.builder()
                .sessionId(sessionId)
                .username(sshShellHelper.getSshSession().getUsername())
                .queryParam(pageQuery)
                .build();
        queryServer(param);
    }

    @SuppressWarnings("SpringShellCommandInspection")
    @ScreenClear
    @SettingContextSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List server info on the before page.", key = "b")
    public void beforePage() {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = SessionCommandContext.getServerQuery();
        if (pageQuery != null) {
            pageQuery.setPage(pageQuery.getPage() > 1 ? pageQuery.getPage() - 1 : pageQuery.getPage());
            QueryServerParam queryServerParam = QueryServerParam.builder()
                    .sessionId(sessionId)
                    .username(sshShellHelper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            queryServer(queryServerParam);
        } else {
            list("", "");
        }
    }

    @SuppressWarnings("SpringShellCommandInspection")
    @ScreenClear
    @SettingContextSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List server info on the next page.", key = "n")
    public void nextPage() {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = SessionCommandContext.getServerQuery();
        if (pageQuery != null) {
            pageQuery.setPage(pageQuery.getPage() + 1);
            QueryServerParam queryServerParam = QueryServerParam.builder()
                    .sessionId(sessionId)
                    .username(sshShellHelper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            queryServer(queryServerParam);
        } else {
            list("", "");
        }
    }

}