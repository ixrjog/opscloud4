package com.baiyi.opscloud.sshserver.command.base;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.sshcore.account.SshAccount;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SimpleTable;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.command.context.ListServerCommand;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.etc.ColorAligner;
import com.baiyi.opscloud.sshserver.packer.SshServerPacker;
import com.baiyi.opscloud.sshserver.util.ServerTableUtil;
import com.baiyi.opscloud.sshserver.util.SessionUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.table.BorderStyle;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.sshserver.util.ServerTableUtil.DIVIDING_LINE;

/**
 * @Author baiyi
 * @Date 2021/7/1 11:07 上午
 * @Version 1.0
 */
public class BaseServerCommand {

    protected static final int PAGE_FOOTER_SIZE = 6;

    @Resource
    protected SshShellHelper helper;

    @Resource
    private ServerService serverService;

    @Resource
    private SshAccount sshAccount;

    @Resource
    private SshServerPacker sshServerPacker;

    private Terminal terminal;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public interface LoginType {
        int LOW_AUTHORITY = 0;
        int HIGH_AUTHORITY = 1;
    }

    protected void doListServer(ListServerCommand commandContext) {
        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("ID")
                .column("Server Name")
                .column("ServerGroup Name")
                .column("Env")
                .column("IP")
                .column("Account")
                .displayHeaders(false)
                .borderStyle(BorderStyle.fancy_light)
                .headerAligner(new ColorAligner(PromptColor.GREEN))
                .useFullBorder(false);

        ServerParam.UserPermissionServerPageQuery pageQuery = commandContext.getQueryParam();
        pageQuery.setUserId(com.baiyi.opscloud.common.util.SessionUtil.getIsAdmin() ? null : com.baiyi.opscloud.common.util.SessionUtil.getUserId());
        pageQuery.setLength(terminal.getSize().getRows() - PAGE_FOOTER_SIZE);
        SessionCommandContext.setServerQuery(pageQuery); // 设置上下文
        DataTable<Server> table = serverService.queryUserPermissionServerPage(pageQuery);
        helper.print(ServerTableUtil.TABLE_HEADERS
                , PromptColor.GREEN);
        helper.print(DIVIDING_LINE, PromptColor.GREEN);

        Map<Integer, Integer> idMapper = Maps.newHashMap();
        int id = 1;
        for (ServerVO.Server s : sshServerPacker.wrapToVO(table.getData())) {
            String envName = buildDisplayEnv(s.getEnv());
            idMapper.put(id, s.getId());
            builder.line(Arrays.asList(
                    String.format(" %-6s|", id),
                    String.format(" %-32s|", s.getDisplayName()),
                    String.format(" %-32s|", s.getServerGroup().getName()),
                    String.format(" %-20s|", envName),
                    String.format(" %-31s|", buildDisplayIp(s)),
                    buildDisplayAccount(s, com.baiyi.opscloud.common.util.SessionUtil.getIsAdmin())));
            id++;
        }
        SessionCommandContext.setIdMapper(idMapper);
        helper.print(helper.renderTable(builder.build()));
        helper.print(ServerTableUtil.buildPagination(table.getTotalNum(),
                pageQuery.getPage(),
                pageQuery.getLength()),
                PromptColor.GREEN);
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

    protected String buildSessionId() {
        return SessionUtil.buildSessionId(helper.getSshSession().getIoSession());
    }
}
