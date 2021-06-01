package com.baiyi.caesar.facade;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.service.server.ServerGroupService;
import com.baiyi.caesar.service.server.ServerService;
import com.github.fonimus.ssh.shell.SimpleTable;
import com.github.fonimus.ssh.shell.SshShellHelper;
import com.github.fonimus.ssh.shell.commands.SshShellComponent;
import com.github.fonimus.ssh.shell.interactive.Interactive;
import com.github.fonimus.ssh.shell.interactive.KeyBinding;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.session.ServerSession;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/31 4:58 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
public class CommandFacade {


    @Resource
    private SshShellHelper helper;

    @Resource
    private ServerService serverService;


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

        String result =helper.renderTable(builder.build());
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

    /**
     * Interactive command example
     *
     * @param fullscreen fullscreen mode
     * @param delay      delay in ms
     */
    @ShellMethod("Interactive command")
    public void login(boolean fullscreen, @ShellOption(defaultValue = "25") long delay) {

        KeyBinding binding = KeyBinding.builder()
                .description("K binding example")
                .key("k").input(() -> log.info("In specific action triggered by key 'k' !")).build();

        Interactive interactive = Interactive.builder().input((size, currentDelay) -> {
            log.info("In interactive command for input...");
            List<AttributedString> lines = new ArrayList<>();
            AttributedStringBuilder sb = new AttributedStringBuilder(size.getColumns());

            sb.append("\nCurrent time", AttributedStyle.BOLD).append(" : ");
            sb.append(String.format("%8tT", new Date()));

            lines.add(sb.toAttributedString());

            SecureRandom sr = new SecureRandom();
            lines.add(new AttributedStringBuilder().append(helper.progress(sr.nextInt(100)),
                    AttributedStyle.DEFAULT.foreground(sr.nextInt(6) + 1)).toAttributedString());
            lines.add(AttributedString.fromAnsi(SshShellHelper.INTERACTIVE_LONG_MESSAGE + "\n"));

            return lines;
        }).binding(binding).fullScreen(fullscreen).refreshDelay(delay).build();

        helper.interactive(interactive);
    }
}
