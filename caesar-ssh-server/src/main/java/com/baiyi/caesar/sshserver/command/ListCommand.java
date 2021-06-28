package com.baiyi.caesar.sshserver.command;

import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.domain.param.server.ServerParam;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.service.auth.AuthRoleService;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.sshcore.account.SshAccount;
import com.baiyi.caesar.sshserver.PromptColor;
import com.baiyi.caesar.sshserver.SimpleTable;
import com.baiyi.caesar.sshserver.SshShellHelper;
import com.baiyi.caesar.sshserver.annotation.InvokeSessionUser;
import com.baiyi.caesar.sshserver.command.context.ListCommandContext;
import com.baiyi.caesar.sshserver.command.etc.ColorAligner;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
@ShellCommandGroup("List")
public class ListCommand {

    @Resource
    private SshShellHelper helper;

    @Resource
    private ServerService serverService;

    @Resource
    private SshAccount sshAccount;



    @Resource
    private SshServerPacker sshServerPacker;

    @Resource
    private AuthRoleService authRoleService;

    private Terminal terminal;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    private interface LoginType {
        int LOW_AUTHORITY = 0;
        int HIGH_AUTHORITY = 1;
    }

    private static final Map<String, ServerParam.UserPermissionServerPageQuery> userSessionServerQueryContainer = Maps.newConcurrentMap();

    private void doListServer(ListCommandContext commandContext) {
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

        ServerParam.UserPermissionServerPageQuery pageQuery = commandContext.getQueryParam();
        pageQuery.setUserId(com.baiyi.caesar.common.util.SessionUtil.getIsAdmin() ? null : com.baiyi.caesar.common.util.SessionUtil.getUserId());
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
                    buildDisplayAccount(s, com.baiyi.caesar.common.util.SessionUtil.getIsAdmin())));
        });
        helper.print(helper.renderTable(builder.build()));
        helper.print(TableUtil.buildPagination(table.getTotalNum(),
                pageQuery.getPage(),
                pageQuery.getLength()),
                PromptColor.GREEN);
    }

    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List server", key = {"ls", "list"})
    public void listServer(@ShellOption(help = "ServerName", defaultValue = "") String name, @ShellOption(help = "IP", defaultValue = "") String ip) {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = ServerParam.UserPermissionServerPageQuery.builder()
                .name(name)
                .queryIp(ip)
                .build();
        pageQuery.setPage(1);
        ListCommandContext commandContext = ListCommandContext.builder()
                .sessionId(sessionId)
                .username(helper.getSshSession().getUsername())
                .queryParam(pageQuery)
                .build();
        doListServer(commandContext);
    }

    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List server before page", key = "b")
    public void beforePage() {
        String sessionId = buildSessionId();
        if (userSessionServerQueryContainer.containsKey(sessionId)) {
            ServerParam.UserPermissionServerPageQuery pageQuery = userSessionServerQueryContainer.get(sessionId);
            pageQuery.setPage(pageQuery.getPage() > 1 ? pageQuery.getPage() - 1 : pageQuery.getPage());
            ListCommandContext commandContext = ListCommandContext.builder()
                    .sessionId(sessionId)
                    .username(helper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            doListServer(commandContext);
        } else {
            listServer("", "");
        }
    }

    /**
     * OPS角色以上即认定为系统管理员
     *
     * @return
     */
    private boolean isAdmin() {
        int accessLevel = authRoleService.getRoleAccessLevelByUsername(com.baiyi.caesar.common.util.SessionUtil.getUsername());
        return accessLevel >= AccessLevel.OPS.getLevel();
    }


    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List server next page", key = "n")
    public void nextPage() {
        String sessionId = buildSessionId();
        if (userSessionServerQueryContainer.containsKey(sessionId)) {
            ServerParam.UserPermissionServerPageQuery pageQuery = userSessionServerQueryContainer.get(sessionId);
            pageQuery.setPage(pageQuery.getPage() + 1);
            ListCommandContext commandContext = ListCommandContext.builder()
                    .sessionId(sessionId)
                    .username(helper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            doListServer(commandContext);
        } else {
            listServer("", "");
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

    private String buildSessionId() {
        return SessionUtil.buildSessionId(helper.getSshSession().getIoSession());
    }
}
