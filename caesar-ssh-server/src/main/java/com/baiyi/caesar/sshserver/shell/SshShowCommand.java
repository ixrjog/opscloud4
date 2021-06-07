package com.baiyi.caesar.sshserver.shell;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Env;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.domain.param.server.ServerParam;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.service.sys.EnvService;
import com.baiyi.caesar.sshcore.account.SshAccount;
import com.github.fonimus.ssh.shell.PromptColor;
import com.github.fonimus.ssh.shell.SimpleTable;
import com.github.fonimus.ssh.shell.SshShellHelper;
import com.github.fonimus.ssh.shell.commands.ColorAligner;
import com.github.fonimus.ssh.shell.commands.SshShellComponent;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/7 11:47 上午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Show")
public class SshShowCommand {

    @Resource
    private SshShellHelper helper;

    @Resource
    private ServerService serverService;

    @Resource
    private EnvService envService;
    @Resource
    private SshAccount sshAccount;

    private interface LoginType {
        int LOW_AUTHORITY = 0;
        int HIGH_AUTHORITY = 1;
    }

    @ShellMethod(value = "Show server", key = "show")
    public String showServer(@ShellOption(help = "ServerName", defaultValue = "") String name) {
        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("id\t")
                .column("| name\t")
                .column("| sn\t")
                .column("| env\t")
                .column("| ip\t")
                .column("| account\t")
                .headerAligner(new ColorAligner(PromptColor.GREEN))
                .borderStyle(BorderStyle.fancy_light)
                .useFullBorder(false);

        ServerParam.UserPermissionServerPageQuery pageQuery = ServerParam.UserPermissionServerPageQuery.builder()
                //  .userId(1)
                .name(name)
                .build();

        pageQuery.setLength(100);
        pageQuery.setPage(1);

        DataTable<Server> table = serverService.queryUserPermissionServerPage(pageQuery);
        table.getData().forEach(s -> {
            Env env = envService.getByEnvType(s.getEnvType());
            builder.line(Arrays.asList(s.getId() + "\t", s.getName() + "\t\t", s.getSerialNumber() + "\t\t", env.getEnvName() + "\t\t", buildDisplayIp(s), buildDisplayAccount(s, true)));
        });
        builder.borderStyle(BorderStyle.air);
        return helper.renderTable(builder.build());
    }

    public static String buildDisplayIp(Server server) {
        if (!StringUtils.isEmpty(server.getPublicIp())) {
            return Joiner.on("/").join(server.getPublicIp(), server.getPrivateIp());
        } else {
            return server.getPrivateIp() + "\t";
        }
    }

    private String buildDisplayAccount(Server server, boolean isAdmin) {
        String displayAccount = null;
        Map<Integer, List<ServerAccount>> accountCatMap = sshAccount.getServerAccountCatMap(server.getId());

        if (accountCatMap.containsKey(LoginType.LOW_AUTHORITY)) {
            displayAccount = Joiner.on(",").skipNulls().join(displayAccount, accountCatMap.get(LoginType.LOW_AUTHORITY).stream().map(a ->
                    SshShellHelper.getColoredMessage(a.getUsername(), PromptColor.GREEN)
            ).collect(Collectors.toList()));
        }

        if (accountCatMap.containsKey(LoginType.HIGH_AUTHORITY)) {
            displayAccount = Joiner.on(",").skipNulls().join(displayAccount, accountCatMap.get(LoginType.HIGH_AUTHORITY).stream().map(a ->
                    SshShellHelper.getColoredMessage(a.getUsername(), PromptColor.RED)
            ).collect(Collectors.toList()));
        }
        return displayAccount + "\t";
    }
}
