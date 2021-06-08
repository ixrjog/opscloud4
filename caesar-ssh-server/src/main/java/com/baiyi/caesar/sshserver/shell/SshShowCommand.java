package com.baiyi.caesar.sshserver.shell;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Env;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.domain.param.server.ServerParam;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.service.sys.EnvService;
import com.baiyi.caesar.sshcore.account.SshAccount;
import com.baiyi.caesar.sshserver.util.SessionUtil;
import com.baiyi.caesar.sshserver.util.TableUtil;
import com.github.fonimus.ssh.shell.*;
import com.github.fonimus.ssh.shell.commands.ColorAligner;
import com.github.fonimus.ssh.shell.commands.SshShellComponent;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.caesar.sshserver.util.TableUtil.DIVIDING_LINE;

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

    private static final Map<String, ServerParam.UserPermissionServerPageQuery> userSessionServerQueryContainer = Maps.newConcurrentMap();

    @ShellMethod(value = "Show server", key = "show")
    public String showServer(@ShellOption(help = "ServerName", defaultValue = "") String name, @ShellOption(help = "IP", defaultValue = "") String ip) {
        String sessionId = buildSessionId();
        Terminal terminal = getTerminal();
        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("id")
                .column("name")
                .column("sn")
                .column("env")
                .column("ip")
                .column("account")
                .displayHeaders(false)
                .borderStyle(BorderStyle.fancy_light)
                .headerAligner(new ColorAligner(PromptColor.GREEN))
                .useFullBorder(false);

        ServerParam.UserPermissionServerPageQuery pageQuery = ServerParam.UserPermissionServerPageQuery.builder()
                //  .userId(1)
                .name(name)
                .queryIp(ip)
                .build();
        int length = terminal.getSize().getRows() - 6;
        int page = 1;
        pageQuery.setLength(length);

        pageQuery.setPage(page);

        userSessionServerQueryContainer.put(sessionId, pageQuery);

        DataTable<Server> table = serverService.queryUserPermissionServerPage(pageQuery);

        helper.print(TableUtil.TABLE_HEADERS
                , PromptColor.GREEN);

        helper.print(DIVIDING_LINE, PromptColor.GREEN);

        table.getData().forEach(s -> {
            Env env = envService.getByEnvType(s.getEnvType());
            String envName = buildDisplayEnv(env);
            Map<Integer, List<ServerAccount>> accountCatMap = sshAccount.getServerAccountCatMap(s.getId());

            builder.line(Arrays.asList(
                    String.format(" %-6s|", s.getId()),
                    String.format(" %-32s|", s.getName()),
                    String.format(" %-6s|", s.getSerialNumber()),
                    String.format(" %-20s|", envName),
                    String.format(" %-31s|", buildDisplayIp(s)),
                    buildDisplayAccount(s, true)));
        });


        helper.print(helper.renderTable(builder.build()));

        helper.print(TableUtil.buildPagination(table.getTotalNum(), page, length), PromptColor.GREEN);
        return "";
    }


    @ShellMethod(value = "Show server before page", key = "b")
    public String beforePage() {
        String sessionId = buildSessionId();
        Terminal terminal = getTerminal();
        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("id")
                .column("name")
                .column("sn")
                .column("env")
                .column("ip")
                .column("account")
                .displayHeaders(false)
                .borderStyle(BorderStyle.fancy_light)
                .headerAligner(new ColorAligner(PromptColor.GREEN))
                .useFullBorder(false);

        ServerParam.UserPermissionServerPageQuery pageQuery = userSessionServerQueryContainer.get(sessionId);
        int length = terminal.getSize().getRows() - 6;
        int page = pageQuery.getPage() > 1 ? pageQuery.getPage() - 1 : pageQuery.getPage();
        pageQuery.setLength(length);
        pageQuery.setPage(page);

        DataTable<Server> table = serverService.queryUserPermissionServerPage(pageQuery);

        helper.print(TableUtil.TABLE_HEADERS
                , PromptColor.GREEN);

        helper.print(DIVIDING_LINE, PromptColor.GREEN);

        table.getData().forEach(s -> {
            Env env = envService.getByEnvType(s.getEnvType());
            String envName = buildDisplayEnv(env);
            Map<Integer, List<ServerAccount>> accountCatMap = sshAccount.getServerAccountCatMap(s.getId());

            builder.line(Arrays.asList(
                    String.format(" %-6s|", s.getId()),
                    String.format(" %-32s|", s.getName()),
                    String.format(" %-6s|", s.getSerialNumber()),
                    String.format(" %-20s|", envName),
                    String.format(" %-31s|", buildDisplayIp(s)),
                    buildDisplayAccount(s, true)));
        });


        helper.print(helper.renderTable(builder.build()));

        helper.print(TableUtil.buildPagination(table.getTotalNum(), page, length), PromptColor.GREEN);
        return "";
    }


    @ShellMethod(value = "Show server next page", key = "n")
    public String nextPage() {
        String sessionId = buildSessionId();
        Terminal terminal = getTerminal();
        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("id")
                .column("name")
                .column("sn")
                .column("env")
                .column("ip")
                .column("account")
                .displayHeaders(false)
                .borderStyle(BorderStyle.fancy_light)
                .headerAligner(new ColorAligner(PromptColor.GREEN))
                .useFullBorder(false);

        ServerParam.UserPermissionServerPageQuery pageQuery = userSessionServerQueryContainer.get(sessionId);
        int length = terminal.getSize().getRows() - 6;
        int page = pageQuery.getPage() + 1;
        pageQuery.setLength(length);
        pageQuery.setPage(page);

        DataTable<Server> table = serverService.queryUserPermissionServerPage(pageQuery);

        helper.print(TableUtil.TABLE_HEADERS
                , PromptColor.GREEN);

        helper.print(DIVIDING_LINE, PromptColor.GREEN);

        table.getData().forEach(s -> {
            Env env = envService.getByEnvType(s.getEnvType());
            String envName = buildDisplayEnv(env);
            Map<Integer, List<ServerAccount>> accountCatMap = sshAccount.getServerAccountCatMap(s.getId());

            builder.line(Arrays.asList(
                    String.format(" %-6s|", s.getId()),
                    String.format(" %-32s|", s.getName()),
                    String.format(" %-6s|", s.getSerialNumber()),
                    String.format(" %-20s|", envName),
                    String.format(" %-31s|", buildDisplayIp(s)),
                    buildDisplayAccount(s, true)));
        });


        helper.print(helper.renderTable(builder.build()));

        helper.print(TableUtil.buildPagination(table.getTotalNum(), page, length), PromptColor.GREEN);
        return "";
    }


    private static String buildDisplayEnv(Env env) {
        if (env.getPromptColor() == null) {
            return env.getEnvName();
        } else {
            return "[" + (new AttributedStringBuilder()).append(env.getEnvName(), AttributedStyle.DEFAULT.foreground(env.getPromptColor())).toAnsi() + "]";
        }
    }

    private static String buildDisplayIp(Server server) {
        if (!StringUtils.isEmpty(server.getPublicIp())) {
            return Joiner.on("/").join(server.getPublicIp(), server.getPrivateIp());
        } else {
            return server.getPrivateIp();
        }
    }

    private String buildDisplayAccount(Server server, boolean isAdmin) {
        String displayAccount = "";
        Map<Integer, List<ServerAccount>> accountCatMap = sshAccount.getServerAccountCatMap(server.getId());
        if (accountCatMap.containsKey(LoginType.LOW_AUTHORITY)) {
            displayAccount = Joiner.on(" ").skipNulls().join(accountCatMap.get(LoginType.LOW_AUTHORITY).stream().map(a ->
                    "[" + SshShellHelper.getColoredMessage(a.getUsername(), PromptColor.GREEN) + "]"
            ).collect(Collectors.toList()));
        }

        if (accountCatMap.containsKey(LoginType.HIGH_AUTHORITY)) {
            displayAccount = Joiner.on(" ").skipNulls().join(displayAccount, accountCatMap.get(LoginType.HIGH_AUTHORITY).stream().map(a ->
                     SshShellHelper.getColoredMessage(a.getUsername(), PromptColor.RED)
            ).collect(Collectors.toList()));
        }
        return displayAccount;
    }


    private Terminal getTerminal() {
        SshContext sshContext = (SshContext) SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        if (sshContext == null) {
            throw new IllegalStateException("Unable to find ssh context");
        } else {
            return sshContext.getTerminal();
        }
    }

    private String buildSessionId() {
        return SessionUtil.buildSessionId(helper.getSshSession().getIoSession());
    }
}
