/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiyi.opscloud.sshserver.commands.system;

import com.baiyi.opscloud.sshserver.commands.SshShellComponent;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.commands.AbstractCommand;
import com.baiyi.opscloud.sshserver.commands.ColorAligner;
import com.baiyi.opscloud.sshserver.interactive.Interactive;
import com.baiyi.opscloud.sshserver.interactive.KeyBinding;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;
import org.springframework.shell.table.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.sshserver.SshShellHelper.*;

/**
 * Jvm command
 */
@SshShellComponent
@ShellCommandGroup("System Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + SystemCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class SystemCommand extends AbstractCommand {

    public static final String GROUP = "system";
    private static final String COMMAND_SYSTEM_ENV = GROUP + "-env";
    private static final String COMMAND_SYSTEM_PROPERTIES = GROUP + "-properties";
    private static final String COMMAND_SYSTEM_THREADS = GROUP + "-threads";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss");

    public static final String SPLIT_REGEX = "[:;]";

    public SystemCommand(SshShellHelper helper, SshShellProperties properties) {
        super(helper, properties, properties.getCommands().getSystem());
    }

    @ShellMethod(key = COMMAND_SYSTEM_ENV, value = "List system environment.")
    @ShellMethodAvailability("jvmEnvAvailability")
    public Object jvmEnv(@ShellOption(help = "Simple view", defaultValue = "false") boolean simpleView) {
        if (simpleView) {
            return buildSimple(System.getenv());
        }
        return buildTable(System.getenv()).render(helper.terminalSize().getRows());
    }

    @ShellMethod(key = COMMAND_SYSTEM_PROPERTIES, value = "List system properties.")
    @ShellMethodAvailability("jvmPropertiesAvailability")
    public Object jvmProperties(@ShellOption(help = "Simple view", defaultValue = "false") boolean simpleView) {
        Map<String, String> map =
                System.getProperties().entrySet().stream().filter(e -> e.getKey() != null)
                        .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue() != null ?
                                e.getValue().toString() : ""));
        if (simpleView) {
            return buildSimple(map);
        }
        return buildTable(map).render(helper.terminalSize().getRows());
    }

    private String buildSimple(Map<String, String> mapParam) {
        Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        map.putAll(mapParam);
        int maxColumn = helper.terminalSize().getRows() - 3 / 2;
        StringBuilder sb = new StringBuilder();
        int max = -1;
        for (String s : map.keySet()) {
            if (s.length() > max && s.length() < maxColumn) {
                max = s.length();
            }
        }

        for (Map.Entry<String, String> e : map.entrySet()) {
            sb.append(String.format("%-" + max + "s", e.getKey())).append(" | ").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }

    private Table buildTable(Map<String, String> mapParam) {
        Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        map.putAll(mapParam);
        String[][] data = new String[map.size() + 1][2];
        TableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);

        data[0][0] = "Key";
        data[0][1] = "Value";
        tableBuilder.on(at(0, 0)).addAligner(SimpleHorizontalAligner.center);
        tableBuilder.on(at(0, 1)).addAligner(SimpleHorizontalAligner.center);
        int i = 1;
        for (Map.Entry<String, String> e : map.entrySet()) {
            data[i][0] = e.getKey();
            data[i][1] = e.getValue();
            tableBuilder.on(at(i, 0)).addAligner(SimpleHorizontalAligner.center);
            tableBuilder.on(at(i, 0)).addAligner(SimpleVerticalAligner.middle);
            if (e.getKey().toLowerCase().contains("path") || e.getKey().toLowerCase().contains("dirs")) {
                tableBuilder.on(at(i, 1)).addSizer((raw, tableWidth, nbColumns) -> extent(raw));
                tableBuilder.on(at(i, 1)).addFormatter(value -> value == null ? new String[]{""}
                        : value.toString().split(SPLIT_REGEX));
            }
            i++;
        }
        return tableBuilder.addFullBorder(BorderStyle.fancy_double).build();
    }

    private static SizeConstraints.Extent extent(String[] raw) {
        int max = 0;
        int min = 0;
        for (String line : raw) {
            String[] words = line.split(SPLIT_REGEX);
            for (String word : words) {
                min = Math.max(min, word.length());
            }
            max = Math.max(max, line.length());
        }
        return new SizeConstraints.Extent(min, max);
    }

    @ShellMethod(key = COMMAND_SYSTEM_THREADS, value = "List jvm threads.")
    @ShellMethodAvailability("threadsAvailability")
    public String threads(@ShellOption(help = "'list' or 'dump' threads. Default is: list", defaultValue = "list", valueProvider = EnumValueProvider.class) ThreadAction action,
                          @ShellOption(help = "Order by column. Default is: id", defaultValue = "id", valueProvider = EnumValueProvider.class) ThreadColumn orderBy,
                          @ShellOption(help = "Reverse order by column. Default is: false", defaultValue = "false") boolean reverseOrder,
                          @ShellOption(help = "Not interactive. Default is: false", defaultValue = "false") boolean staticDisplay,
                          @ShellOption(help = "Only for DUMP action", defaultValue = ShellOption.NULL) Long threadId) {

        if (action == ThreadAction.dump) {
            Thread th = get(threadId);
            helper.print("Name  : " + th.getName());
            helper.print("State : " + helper.getColored(th.getState().name(), color(th.getState())) + "\n");
            Exception e = new Exception("Thread [" + th.getId() + "] stack trace");
            e.setStackTrace(th.getStackTrace());
            e.printStackTrace(helper.terminalWriter());
            return "";
        }

        if (staticDisplay) {
            return table(orderBy, reverseOrder, false);
        }

        boolean[] finalReverseOrder = {reverseOrder};
        ThreadColumn[] finalOrderBy = {orderBy};

        Interactive.InteractiveBuilder builder = Interactive.builder();
        for (ThreadColumn value : ThreadColumn.values()) {
            String key = value == ThreadColumn.interrupted ? "t" : value.name().toLowerCase().substring(0, 1);
            builder.binding(KeyBinding.builder().description("ORDER_" + value.name()).key(key)
                    .input(() -> {
                        if (value == finalOrderBy[0]) {
                            finalReverseOrder[0] = !finalReverseOrder[0];
                        } else {
                            finalOrderBy[0] = value;
                        }
                    }).build());
        }
        builder.binding(KeyBinding.builder().key("r").description("REVERSE")
                .input(() -> finalReverseOrder[0] = !finalReverseOrder[0]).build());

        helper.interactive(builder.input((size, currentDelay) -> {
            List<AttributedString> lines = new ArrayList<>(size.getRows());

            lines.add(new AttributedStringBuilder()
                    .append("Time: ")
                    .append(FORMATTER.format(LocalDateTime.now()), AttributedStyle.BOLD)
                    .append(", refresh delay: ")
                    .append(String.valueOf(currentDelay), AttributedStyle.BOLD)
                    .append(" ms\n")
                    .toAttributedString());

            for (String s : table(finalOrderBy[0], finalReverseOrder[0], true).split("\n")) {
                lines.add(AttributedString.fromAnsi(s));
            }

            lines.add(AttributedString.fromAnsi("Press 'r' to reverse order, first column letter to change order by"));
            String msg = INTERACTIVE_LONG_MESSAGE.length() <= helper.terminalSize().getColumns() ?
                    INTERACTIVE_LONG_MESSAGE : INTERACTIVE_SHORT_MESSAGE;
            lines.add(AttributedString.fromAnsi(msg));

            return lines;
        }).build());
        return "";
    }

    private Thread get(Long threadId) {
        if (threadId == null) {
            throw new IllegalArgumentException("Thread id is mandatory");
        }
        Thread t = getThreads().get(threadId);
        if (t == null) {
            throw new IllegalArgumentException("Could not find thread for id: " + threadId);
        }
        return t;
    }

    private Comparator<? super Thread> comparator(ThreadColumn orderBy, boolean reverseOrder) {
        Comparator<? super Thread> c = switch (orderBy) {
            case priority -> Comparator.comparingDouble(Thread::getPriority);
            case state -> Comparator.comparing(e -> e.getState().name());
            case interrupted -> Comparator.comparing(Thread::isAlive);
            case daemon -> Comparator.comparing(Thread::isDaemon);
            case name -> Comparator.comparing(Thread::getName);
            default -> Comparator.comparingDouble(Thread::getId);
        };
        if (reverseOrder) {
            c = c.reversed();
        }
        return c;
    }

    private PromptColor color(Thread.State state) {
        return switch (state) {
            case RUNNABLE -> PromptColor.GREEN;
            case BLOCKED, TERMINATED -> PromptColor.RED;
            case WAITING, TIMED_WAITING -> PromptColor.CYAN;
            default -> PromptColor.WHITE;
        };
    }

    private String table(ThreadColumn orderBy, boolean reverseOrder, boolean fullscreen) {
        List<Thread> ordered = new ArrayList<>(getThreads().values());
        ordered.sort(comparator(orderBy, reverseOrder));

        // handle maximum rows: 1 line for headers, 3 borders, 3 description lines
        int maxWithHeadersAndBorders = helper.terminalSize().getRows() - 8;
        int tableSize = ordered.size() + 1;
        boolean addDotLine = false;
        if (fullscreen && ordered.size() > maxWithHeadersAndBorders) {
            ordered = ordered.subList(0, maxWithHeadersAndBorders);
            tableSize = maxWithHeadersAndBorders + 2;
            addDotLine = true;
        }

        String[][] data = new String[tableSize][ThreadColumn.values().length];
        TableBuilder tableBuilder = new TableBuilder(new ArrayTableModel(data));

        int i = 0;
        for (ThreadColumn column : ThreadColumn.values()) {
            data[0][i] = column.name();
            tableBuilder.on(at(0, i)).addAligner(SimpleHorizontalAligner.center);
            i++;
        }
        int r = 1;
        for (Thread t : ordered) {
            data[r][0] = String.valueOf(t.getId());
            data[r][1] = String.valueOf(t.getPriority());
            data[r][2] = t.getState().name();
            tableBuilder.on(at(r, 2)).addAligner(new ColorAligner(color(t.getState())));
            data[r][3] = String.valueOf(t.isInterrupted());
            data[r][4] = String.valueOf(t.isDaemon());
            data[r][5] = t.getName();
            r++;
        }
        if (addDotLine) {
            String dots = "...";
            data[r][0] = dots;
            data[r][1] = dots;
            data[r][2] = dots;
            data[r][3] = dots;
            data[r][4] = dots;
            data[r][5] = "... not enough rows to display all threads";
        }
        return tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_double).build().render(helper.terminalSize().getRows());
    }

    private static Map<Long, Thread> getThreads() {
        ThreadGroup root = getRoot();
        Thread[] threads = new Thread[root.activeCount()];
        while (root.enumerate(threads, true) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        Map<Long, Thread> map = new HashMap<>();
        for (Thread thread : threads) {
            if (thread != null) {
                map.put(thread.getId(), thread);
            }
        }
        return map;
    }

    private static ThreadGroup getRoot() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup parent;
        while ((parent = group.getParent()) != null) {
            group = parent;
        }
        return group;
    }

    private Availability threadsAvailability() {
        return availability(GROUP, COMMAND_SYSTEM_THREADS);
    }

    enum ThreadColumn {
        id, priority, state, interrupted, daemon, name
    }

    enum ThreadAction {
        list, dump
    }

    private Availability jvmEnvAvailability() {
        return availability(GROUP, COMMAND_SYSTEM_ENV);
    }

    private Availability jvmPropertiesAvailability() {
        return availability(GROUP, COMMAND_SYSTEM_PROPERTIES);
    }
}
