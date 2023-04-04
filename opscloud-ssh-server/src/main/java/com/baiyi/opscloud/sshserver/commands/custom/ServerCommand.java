package com.baiyi.opscloud.sshserver.commands.custom;


import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.aop.annotation.SettingContextSessionUser;
import com.baiyi.opscloud.sshserver.aop.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.commands.SshShellComponent;
import com.baiyi.opscloud.sshserver.commands.custom.param.ListServerParam;
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
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + ServerCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class ServerCommand extends BaseServerCommand {

    public static final String GROUP = "server";
    private static final String COMMAND_SERVER_LIST = GROUP + "-list";
    private static final String COMMAND_SERVER_LOGIN = GROUP + "-login";

    /**
     * 查询服务器信息
     *
     * @param name serverName
     * @param ip serverIP
     */
    // @ShellMethodAvailability("jmxListAvailability")
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
        ListServerParam param = ListServerParam.builder()
                .sessionId(sessionId)
                .username(sshShellHelper.getSshSession().getUsername())
                .queryParam(pageQuery)
                .build();
        doListServer(param);
    }

}
