package com.baiyi.opscloud.sshserver.command.event;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.event.EventParam;
import com.baiyi.opscloud.event.IEventProcess;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.event.factory.EventFactory;
import com.baiyi.opscloud.sshcore.table.PrettyTable;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.annotation.CheckTerminalSize;
import com.baiyi.opscloud.sshserver.annotation.InvokeSessionUser;
import com.baiyi.opscloud.sshserver.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.command.component.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.event.base.EventContext;
import com.baiyi.opscloud.sshserver.command.event.util.SeverityUtil;
import com.baiyi.opscloud.sshserver.util.ServerTableUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/13 3:19 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Event")
public class EventCommand {

    private Terminal terminal;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @Resource
    protected SshShellHelper helper;

    protected static final int PAGE_FOOTER_SIZE = 6;

    @CheckTerminalSize(cols = 130, rows = 10)
    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "查询事件", key = {"event", "list-event"})
    public void listEvent(@ShellOption(help = "Event Name", defaultValue = "") String name) {
        EventParam.UserPermissionEventPageQuery pageQuery = EventParam.UserPermissionEventPageQuery.builder()
                .name(name)
                .build();
        pageQuery.setPage(1);
        PrettyTable pt = PrettyTable
                .fieldNames("ID",
                        "Severity",
                        "Event Name",
                        "Lastchange Time",
                        "Servers"
                );
        pageQuery.setUserId(com.baiyi.opscloud.common.util.SessionUtil.getIsAdmin() ? null : com.baiyi.opscloud.common.util.SessionUtil.getUserId());
        pageQuery.setLength(terminal.getSize().getRows() - PAGE_FOOTER_SIZE);

        IEventProcess iEventProcess = EventFactory.getIEventProcessByEventType(EventTypeEnum.ZABBIX_PROBLEM);
        DataTable<Event> table = iEventProcess.listEvent(pageQuery);
        Map<Integer, EventContext> eventMapper = Maps.newHashMap();
        int id = 1;
        for (Event event : table.getData()) {
            EventContext eventContext = BeanCopierUtil.copyProperties(event, EventContext.class);
            eventMapper.put(id, eventContext);
            pt.addRow(id,
                    SeverityUtil.toTerminalStr(eventContext.getPriority()),
                    eventContext.getEventName(),
                    TimeUtil.toGmtDate(eventContext.getLastchangeTime()),
                    ""
            );
            id++;
        }
        SessionCommandContext.setEventMapper(eventMapper);
        helper.print(pt.toString());
        helper.print(ServerTableUtil.buildPagination(table.getTotalNum(),
                        pageQuery.getPage(),
                        pageQuery.getLength()),
                PromptColor.GREEN);
    }

}
