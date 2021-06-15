package com.baiyi.caesar.sshserver.commands;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Env;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.server.ServerParam;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.sshcore.account.SshAccount;
import com.baiyi.caesar.sshserver.*;
import com.baiyi.caesar.sshserver.commands.context.ShowCommandContext;
import com.baiyi.caesar.sshserver.commands.etc.ColorAligner;
import com.baiyi.caesar.sshserver.packer.SshServerPacker;
import com.baiyi.caesar.sshserver.util.SessionUtil;
import com.baiyi.caesar.sshserver.util.TableUtil;
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
public class ShowCommand {

    @Resource
    private SshShellHelper helper;

    @Resource
    private ServerService serverService;

    @Resource
    private SshAccount sshAccount;

    @Resource
    private UserService userService;

    @Resource
    private SshServerPacker sshServerPacker;

    private interface LoginType {
        int LOW_AUTHORITY = 0;
        int HIGH_AUTHORITY = 1;
    }

    private static final Map<String, ServerParam.UserPermissionServerPageQuery> userSessionServerQueryContainer = Maps.newConcurrentMap();

    private void doShowServer(ShowCommandContext commandContext) {
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
        User user = userService.getByUsername(commandContext.getUsername());

        com.baiyi.caesar.common.util.SessionUtil.setUserId(user.getId());
        com.baiyi.caesar.common.util.SessionUtil.setUsername(user.getUsername());

        if (user == null || !user.getIsActive()) {
            helper.print("未经授权的访问！", PromptColor.RED);
            return;
        }
        ServerParam.UserPermissionServerPageQuery pageQuery = commandContext.getQueryParam();
        pageQuery.setUserId(user.getId());
        pageQuery.setLength(terminal.getSize().getRows() - 6);
        userSessionServerQueryContainer.put(commandContext.getSessionId(), pageQuery);
        DataTable<Server> table = serverService.queryUserPermissionServerPage(pageQuery);
        helper.print(TableUtil.TABLE_HEADERS
                , PromptColor.GREEN);
        helper.print(DIVIDING_LINE, PromptColor.GREEN);

        sshServerPacker.wrapToVO(table.getData()).forEach(s -> {

            String envName = buildDisplayEnv(s.getEnv());
            builder.line(Arrays.asList(
                    String.format(" %-6s|", s.getId()),
                    String.format(" %-32s|", s.getDisplayName()),
                    String.format(" %-32s|", s.getServerGroup().getName()),
                    String.format(" %-20s|", envName),
                    String.format(" %-31s|", buildDisplayIp(s)),
                    buildDisplayAccount(s, false)));
        });
        helper.print(helper.renderTable(builder.build()));
        helper.print(TableUtil.buildPagination(table.getTotalNum(),
                pageQuery.getPage(),
                pageQuery.getLength()),
                PromptColor.GREEN);
    }

    @ShellMethod(value = "Show server", key = {"ls", "list", "host", "s", "show"})
    public void showServer(@ShellOption(help = "ServerName", defaultValue = "") String name, @ShellOption(help = "IP", defaultValue = "") String ip) {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = ServerParam.UserPermissionServerPageQuery.builder()
                .name(name)
                .queryIp(ip)
                .build();
        pageQuery.setPage(1);
        ShowCommandContext commandContext = ShowCommandContext.builder()
                .sessionId(sessionId)
                .username(helper.getSshSession().getUsername())
                .queryParam(pageQuery)
                .build();
        doShowServer(commandContext);
    }

    @ShellMethod(value = "Show server before page", key = "b")
    public void beforePage() {
        String sessionId = buildSessionId();
        if (userSessionServerQueryContainer.containsKey(sessionId)) {
            ServerParam.UserPermissionServerPageQuery pageQuery = userSessionServerQueryContainer.get(sessionId);
            pageQuery.setPage(pageQuery.getPage() > 1 ? pageQuery.getPage() - 1 : pageQuery.getPage());
            ShowCommandContext commandContext = ShowCommandContext.builder()
                    .sessionId(sessionId)
                    .username(helper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            doShowServer(commandContext);
        } else {
            showServer("", "");
        }
    }


    @ShellMethod(value = "Show server next page", key = "n")
    public void nextPage() {
        String sessionId = buildSessionId();
        if (userSessionServerQueryContainer.containsKey(sessionId)) {
            ServerParam.UserPermissionServerPageQuery pageQuery = userSessionServerQueryContainer.get(sessionId);
            pageQuery.setPage(pageQuery.getPage() + 1);
            ShowCommandContext commandContext = ShowCommandContext.builder()
                    .sessionId(sessionId)
                    .username(helper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            doShowServer(commandContext);
        } else {
            showServer("", "");
        }
    }

    private static String buildDisplayEnv(Env env) {
        if (env.getPromptColor() == null) {
            return env.getEnvName();
        } else {
            return "[" + (new AttributedStringBuilder()).append(env.getEnvName(), AttributedStyle.DEFAULT.foreground(env.getPromptColor())).toAnsi() + "]";
        }
    }

    private static String buildDisplayEnv(EnvVO.Env env) {
        if (env.getPromptColor() == null) {
            return env.getEnvName();
        } else {
            return "[" + (new AttributedStringBuilder()).append(env.getEnvName(), AttributedStyle.DEFAULT.foreground(env.getPromptColor())).toAnsi() + "]";
        }
    }

    private static String buildDisplayIp(ServerVO.Server server) {
        if (!StringUtils.isEmpty(server.getPublicIp())) {
            return Joiner.on("/").join(server.getPublicIp(), server.getPrivateIp());
        } else {
            return server.getPrivateIp();
        }
    }

    private String buildDisplayAccount(ServerVO.Server server, boolean isAdmin) {
        String displayAccount = "";
        Map<Integer, List<ServerAccount>> accountCatMap = sshAccount.getServerAccountCatMap(server.getId());
        if (accountCatMap.containsKey(LoginType.LOW_AUTHORITY)) {
            displayAccount = Joiner.on(" ").skipNulls().join(accountCatMap.get(LoginType.LOW_AUTHORITY).stream().map(a ->
                    "[" + SshShellHelper.getColoredMessage(a.getUsername(), PromptColor.GREEN) + "]"
            ).collect(Collectors.toList()));
        }
        if (isAdmin || "admin".equalsIgnoreCase(server.getServerGroup().getUserPermission().getPermissionRole()))
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
