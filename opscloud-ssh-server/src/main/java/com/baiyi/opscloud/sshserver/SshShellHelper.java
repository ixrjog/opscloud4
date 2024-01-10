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

package com.baiyi.opscloud.sshserver;

import com.baiyi.opscloud.sshserver.auth.SshAuthentication;
import com.baiyi.opscloud.sshserver.interactive.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.session.ServerSession;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.impl.AbstractPosixTerminal;
import org.jline.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.table.*;

import java.io.PrintWriter;
import java.util.*;

/**
 * Ssh shell helper for user interactions and authorities check
 */
@Slf4j
public class SshShellHelper {

    public static final String INTERACTIVE_LONG_MESSAGE = "Please press key 'q' to quit, '+' and '-' to increase or " +
            "decrease refresh delay";

    public static final String INTERACTIVE_SHORT_MESSAGE = "'q': quit, '+'|'-': increase|decrease refresh";

    public static final String EXIT = "_EXIT";

    public static final List<String> DEFAULT_CONFIRM_WORDS = Arrays.asList("y", "yes");

    private static final List<Aligner> DEFAULT_ALIGNERS = Arrays.asList(
            SimpleHorizontalAligner.center, SimpleVerticalAligner.middle
    );

    private final List<String> confirmWords;

    @Autowired
    @Lazy
    private Terminal defaultTerminal;

    @Autowired
    @Lazy
    private LineReader defaultLineReader;

    /**
     * Constructor with confirmation words
     *
     * @param confirmWords confirmation words
     */
    public SshShellHelper(List<String> confirmWords) {
        this.confirmWords = confirmWords != null ? confirmWords : DEFAULT_CONFIRM_WORDS;
    }

    /**
     * Color message with given color
     *
     * @param message message to return
     * @param color   color to print
     * @return colored message
     */
    public String getColored(String message, PromptColor color) {
        return getColoredMessage(message, color);
    }

    /**
     * Color message with given color
     *
     * @param message message to return
     * @param color   color to print
     * @return colored message
     */
    public static String getColoredMessage(String message, PromptColor color) {
        return new AttributedStringBuilder().append(message,
                AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle())).toAnsi();
    }

    /**
     * Color message with given background color
     *
     * @param message         message to return
     * @param backgroundColor background color to print
     * @return colored message
     */
    public String getBackgroundColored(String message, PromptColor backgroundColor) {
        return getBackgroundColoredMessage(message, backgroundColor);
    }

    /**
     * Color message with given background color
     *
     * @param message         message to return
     * @param backgroundColor background color to print
     * @return colored message
     */
    public static String getBackgroundColoredMessage(String message, PromptColor backgroundColor) {
        return new AttributedStringBuilder().append(message,
                AttributedStyle.DEFAULT.background(backgroundColor.toJlineAttributedStyle())).toAnsi();
    }

    /**
     * @param message      confirmation message
     * @param confirmWords (optional) confirmation words, default are {@link SshShellHelper#DEFAULT_CONFIRM_WORDS},
     *                     or configured in {@link SshShellProperties}
     * @return whether it has been confirmed
     */
    public boolean confirm(String message, String... confirmWords) {
        return confirm(message, false, confirmWords);
    }

    /**
     * @param message       confirmation message
     * @param caseSensitive should be case sensitive or not
     * @param confirmWords  (optional) confirmation words, default are {@link SshShellHelper#DEFAULT_CONFIRM_WORDS},
     *                      or configured in {@link SshShellProperties}
     * @return whether it has been confirmed
     */
    public boolean confirm(String message, boolean caseSensitive, String... confirmWords) {
        String response = read(message);
        List<String> confirm = this.confirmWords;
        if (confirmWords != null && confirmWords.length > 0) {
            confirm = Arrays.asList(confirmWords);
        }
        for (String c : confirm) {
            if (caseSensitive && c.equals(response)) {
                return true;
            } else if (!caseSensitive && c.equalsIgnoreCase(response)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Read from terminal
     *
     * @return response read from terminal
     */
    public String read() {
        return read(null);
    }

    /**
     * Print confirmation message and get response
     *
     * @param message message to print
     * @return response read from terminal
     */
    public String read(String message) {
        LineReader lr = reader();
        if (message != null) {
            lr.getTerminal().writer().println(message);
        }
        lr.readLine();
        if (lr.getTerminal() instanceof AbstractPosixTerminal) {
            lr.getTerminal().writer().println();
        }
        return lr.getParsedLine().line();
    }

    /**
     * Color message with color {@link PromptColor#GREEN}
     *
     * @param message message to return
     * @return colored message
     */
    public String getSuccess(String message) {
        return getColored(message, PromptColor.GREEN);
    }

    /**
     * Color message with color {@link PromptColor#CYAN}
     *
     * @param message message to return
     * @return colored message
     */
    public String getInfo(String message) {
        return getColored(message, PromptColor.CYAN);
    }

    /**
     * Color message with color {@link PromptColor#YELLOW}
     *
     * @param message message to return
     * @return colored message
     */
    public String getWarning(String message) {
        return getColored(message, PromptColor.YELLOW);
    }

    /**
     * Color message with color {@link PromptColor#RED}
     *
     * @param message message to return
     * @return colored message
     */
    public String getError(String message) {
        return getColored(message, PromptColor.RED);
    }

    /**
     * Print message with color {@link PromptColor#GREEN}
     *
     * @param message message to print
     */
    public void printSuccess(String message) {
        print(message, PromptColor.GREEN);
    }

    /**
     * Print message with color {@link PromptColor#CYAN}
     *
     * @param message message to print
     */
    public void printInfo(String message) {
        print(message, PromptColor.CYAN);
    }

    /**
     * Print message with color {@link PromptColor#YELLOW}
     *
     * @param message message to print
     */
    public void printWarning(String message) {
        print(message, PromptColor.YELLOW);
    }

    /**
     * Print message with color {@link PromptColor#RED}
     *
     * @param message message to print
     */
    public void printError(String message) {
        print(message, PromptColor.RED);
    }

    /**
     * Print in the console
     *
     * @param message message to print
     */
    public void print(String message) {
        print(message, null);
    }

    /**
     * Print in the console
     *
     * @param message message to print
     * @param color   (optional) prompt color
     */
    public void print(String message, PromptColor color) {
        String toPrint = message;
        if (color != null) {
            toPrint = getColored(message, color);
        }
        terminal().writer().println(toPrint);
    }

    /**
     * Renders table in current terminal
     *
     * @param simpleTable simple table
     * @return table as string
     */
    public String renderTable(SimpleTable simpleTable) {
        return renderTable(buildTable(simpleTable));
    }

    /**
     * Renders table in current terminal
     *
     * @param table built table
     * @return table as string
     */
    public String renderTable(Table table) {
        return table.render(terminalSize().getColumns());
    }

    /**
     * Build table from simple builder
     *
     * @param simpleTable simple table
     * @return table
     */
    public Table buildTable(SimpleTable simpleTable) {
        int nbColumns = simpleTable.getColumns().size();
        if (nbColumns == 0) {
            throw new IllegalArgumentException("Table should have at least one column");
        }
        int nbLines = simpleTable.getLines().size();
        if (simpleTable.isDisplayHeaders()) {
            nbLines++;
        }
        String[][] data = new String[nbLines][simpleTable.getColumns().size()];
        TableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);
        int i = 0;
        if (simpleTable.isDisplayHeaders()) {
            // let first line for headers
            i = 1;
            int c = 0;
            if (simpleTable.getHeaderAligners() == null || simpleTable.getHeaderAligners().isEmpty()) {
                simpleTable.setHeaderAligners(DEFAULT_ALIGNERS);
            }
            for (String header : simpleTable.getColumns()) {
                data[0][c] = header;
                for (Aligner headerAligner : simpleTable.getHeaderAligners()) {
                    tableBuilder.on(at(0, c)).addAligner(headerAligner);
                }
                c++;
            }
        }
        if (simpleTable.getLineAligners() == null || simpleTable.getLineAligners().isEmpty()) {
            simpleTable.setLineAligners(DEFAULT_ALIGNERS);
        }
        for (List<Object> line : simpleTable.getLines()) {
            int c = 0;
            for (Object objValue : line) {
                // ensure we don't have more column in line than in column names
                if (c >= nbColumns) {
                    break;
                }
                String value = "";
                if (objValue != null) {
                    if (objValue instanceof String) {
                        value = (String) objValue;
                    } else {
                        value = objValue.toString();
                    }
                }
                data[i][c] = value;
                for (Aligner lineAligner : simpleTable.getLineAligners()) {
                    tableBuilder.on(at(i, c)).addAligner(lineAligner);
                }
                c++;
            }
            i++;
        }
        if (simpleTable.getTableBuilderListener() != null) {
            simpleTable.getTableBuilderListener().onBuilt(tableBuilder);
        }
        if (simpleTable.isUseFullBorder()) {
            tableBuilder.addFullBorder(simpleTable.getBorderStyle());
        }
        return tableBuilder.build();
    }

    /**
     * Build cell matcher
     * <p>Should be used only once</p>
     *
     * @param row the row
     * @param col the col
     * @return the cell matcher
     */
    public static CellMatcher at(final int row, final int col) {
        return (r, column, model) -> r == row && column == col;
    }

    /**
     * Get ssh authentication containing objects from spring security when configured to 'security'
     *
     * @return authentication from spring authentication, or null of not found in context
     */
    public SshAuthentication getAuthentication() {
        return SshShellCommandFactory.SSH_THREAD_CONTEXT.get().getAuthentication();
    }

    /**
     * Get ssh session
     *
     * @return current ssh session, or null if local prompt
     */
    public ServerSession getSshSession() {
        return SshShellCommandFactory.SSH_THREAD_CONTEXT.get().getSshSession();
    }

    /**
     * Get ssh environment
     *
     * @return current ssh environment, or null if local prompt
     */
    public Environment getSshEnvironment() {
        return SshShellCommandFactory.SSH_THREAD_CONTEXT.get().getSshEnv();
    }

    /**
     * @return true if current command executed in a local prompt
     */
    public boolean isLocalPrompt() {
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        if (sshContext == null) {
            return true;
        }
        return sshContext.isLocalPrompt();
    }

    /**
     * Check that one of the roles is in current authorities
     *
     * @param authorizedRoles authorized roles
     * @return true if role found in authorities
     */
    public boolean checkAuthorities(List<String> authorizedRoles) {
        if (isLocalPrompt()) {
            log.debug("Not an ssh session -> local prompt -> giving all rights");
            return true;
        }
        SshAuthentication auth = SshShellCommandFactory.SSH_THREAD_CONTEXT.get().getAuthentication();
        return checkAuthorities(authorizedRoles, auth != null ? auth.getAuthorities() : null, false);
    }

    /**
     * Check that one of the roles is in authorities
     *
     * @param authorizedRoles           authorized roles
     * @param authorities               current authorities
     * @param authorizedIfNoAuthorities whether to return true if no authorities
     * @return true if role found in authorities
     */
    public boolean checkAuthorities(List<String> authorizedRoles, List<String> authorities,
                                    boolean authorizedIfNoAuthorities) {
        if (authorities == null) {
            // if authorized only -> return false
            return authorizedIfNoAuthorities;
        }
        for (String authority : authorities) {
            String check = authority;
            if (check.startsWith("ROLE_")) {
                check = check.substring(5);
            }
            if (authorizedRoles.contains(check)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get terminal size
     *
     * @return size
     */
    public Size terminalSize() {
        return terminal().getSize();
    }

    /**
     * Display percentage on full terminal line
     *
     * @param percentage current value
     * @return percentage line
     */
    public String progress(int percentage) {
        int current = percentage;
        if (current > 100) {
            current = 100;
            log.warn("Setting percentage to 100 (was: {})", percentage);
        }
        return progress(current, 100);
    }

    /**
     * Display percentage on full terminal line
     *
     * @param current current value
     * @param total   total value
     * @return percentage line
     */
    public String progress(int current, int total) {
        StringBuilder builder = new StringBuilder("[");
        int col = terminalSize().getColumns();
        int max = col - 3;
        if (max < 0) {
            log.warn("Terminal is too small to print progress [columns={}]", col);
            return "";
        }
        int percentage = current * max / total;

        if (percentage > 0) {
            builder.append(String.format("%" + percentage + "s", " ").replaceAll(" ", "="));
        }
        builder.append(">");
        int left = (max - percentage);
        if (left > 0) {
            builder.append(String.format("%" + left + "s", ""));
        }
        return builder.append("]").toString();
    }

    // Interactive command which refreshes automatically

    private static String generateId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Return the terminal writer
     *
     * @return terminal writer
     */
    public PrintWriter terminalWriter() {
        return terminal().writer();
    }

    /**
     * Return the terminal reader history
     *
     * @return history
     */
    public History getHistory() {
        return new DefaultHistory(this.reader());
    }

    /**
     * Interactive
     *
     * @param interactive interactive built command
     */
    public void interactive(Interactive interactive) {
        final long[] refreshDelay = {interactive.getRefreshDelay()};
        int rows = 0;
        final int[] maxLines = {rows};
        Terminal terminal = terminal();
        Display display = new Display(terminal, interactive.isFullScreen());
        Size size = interactive.getSize() != null ? interactive.getSize() : new Size();
        BindingReader bindingReader = new BindingReader(terminal.reader());

        size.copy(new Size(terminal.getSize().getColumns(), terminal.getSize().getRows()));
        Terminal.SignalHandler prevHandler = terminal.handle(Terminal.Signal.WINCH, signal -> {
            int previous = size.getColumns();
            size.copy(new Size(terminal.getSize().getColumns(), rows));
            if (size.getColumns() < previous) {
                display.clear();
            }
            maxLines[0] = display(interactive.getInput(), display, size, refreshDelay[0]).getLines();
        });
        Attributes attr = terminal.enterRawMode();
        try {

            terminal.puts(InfoCmp.Capability.cursor_invisible);
            if (interactive.isFullScreen()) {
                terminal.puts(InfoCmp.Capability.enter_ca_mode);
                terminal.puts(InfoCmp.Capability.keypad_xmit);
                terminal.writer().flush();
            }

            long t0 = System.currentTimeMillis();

            KeyMap<String> keys = new KeyMap<>();
            Map<String, KeyBindingInput> inputs = new HashMap<>();
            Set<String> usedKeys = new HashSet<>();

            if (interactive.isExit()) {
                keys.bind(EXIT, "q");
                inputs.put(EXIT, () -> {
                    // nothing
                });
                usedKeys.add("q");
            }
            if (interactive.isIncrease()) {
                String id = generateId();
                keys.bind(id, "+");
                inputs.put(id, () -> {
                    refreshDelay[0] = refreshDelay[0] + 1000;
                    log.debug("New refresh delay is now: " + refreshDelay[0]);
                });
                usedKeys.add("+");
            }
            if (interactive.isDecrease()) {
                String id = generateId();
                keys.bind(id, "-");
                inputs.put(id, () -> {
                    if (refreshDelay[0] > 1000) {
                        refreshDelay[0] = refreshDelay[0] - 1000;
                        log.debug("New refresh delay is now: " + refreshDelay[0]);
                    } else {
                        log.warn("Cannot decrease delay under 1000 ms");
                    }
                });
                usedKeys.add("-");
            }

            for (KeyBinding binding : interactive.getBindings()) {
                List<String> newKeys = new ArrayList<>();
                for (String key : binding.getKeys()) {
                    if (usedKeys.contains(key)) {
                        log.warn("Binding key not allowed as already used: {}.", key);
                    } else {
                        newKeys.add(key);
                    }
                }
                if (newKeys.isEmpty()) {
                    log.error("None of the keys are allowed {}, action [{}] will not be bound",
                            binding.getDescription(), binding.getKeys());
                } else {
                    String id = generateId();
                    keys.bind(id, newKeys.toArray(new String[0]));
                    inputs.put(id, binding.getInput());
                    usedKeys.addAll(newKeys);
                    log.debug("Binding [{}] added with keys: {}", binding.getDescription(), newKeys);
                }
            }

            String op;
            do {
                DisplayResult result = display(interactive.getInput(), display, size, refreshDelay[0]);
                maxLines[0] = result.getLines();
                checkInterrupted();

                long delta = ((System.currentTimeMillis() - t0) / refreshDelay[0] + 1)
                        * refreshDelay[0] + t0 - System.currentTimeMillis();

                int ch = bindingReader.peekCharacter(delta);
                op = null;
                // 27 is escape char
                if (ch == -1 || ch == 27) {
                    op = EXIT;
                } else if (ch != NonBlockingReader.READ_EXPIRED) {
                    op = bindingReader.readBinding(keys, null, false);
                } else if (result.isStop()) {
                    op = EXIT;
                }
                if (op == null) {
                    continue;
                }

                KeyBindingInput input = inputs.get(op);
                if (input != null) {
                    input.action();
                }
            } while (op == null || !op.equals(EXIT));
        } catch (InterruptedException ie) {
            // Do nothing
        } finally {
            terminal.setAttributes(attr);
            if (prevHandler != null) {
                terminal.handle(Terminal.Signal.WINCH, prevHandler);
            }

            terminal.puts(InfoCmp.Capability.cursor_visible);
            if (interactive.isFullScreen()) {
                terminal.puts(InfoCmp.Capability.exit_ca_mode);
                terminal.puts(InfoCmp.Capability.keypad_local);
                terminal.writer().flush();
            } else {
                for (int i = 0; i < maxLines[0]; i++) {
                    terminal.writer().println();
                }
            }
        }
    }

    private DisplayResult display(InteractiveInput input, Display display, Size size, long currentDelay) {
        display.resize(size.getRows(), size.getColumns());
        DisplayResult result = new DisplayResult();
        List<AttributedString> lines;
        if (input instanceof StoppableInteractiveInput) {
            InteractiveInputIO io = ((StoppableInteractiveInput) input).getIO(size, currentDelay);
            result.setStop(io.isStop());
            lines = io.getLines();
        } else {
            lines = input.getLines(size, currentDelay);
        }
        display.update(lines, 0);
        result.setLines(lines.size());
        return result;
    }

    private void checkInterrupted() throws InterruptedException {
        Thread.yield();
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    private Terminal terminal() {
        if (isLocalPrompt()) {
            // local prompt
            return defaultTerminal;
        }
        return SshShellCommandFactory.SSH_THREAD_CONTEXT.get().getTerminal();
    }

    private LineReader reader() {
        if (isLocalPrompt()) {
            // local prompt
            return defaultLineReader;
        }
        return SshShellCommandFactory.SSH_THREAD_CONTEXT.get().getLineReader();
    }

    /**
     * Display result POJO
     */
    @Data
    public static class DisplayResult {

        private int lines;

        private boolean stop;
    }

    public void setDefaultTerminal(Terminal defaultTerminal) {
        this.defaultTerminal = defaultTerminal;
    }

    public void setDefaultLineReader(LineReader defaultLineReader) {
        this.defaultLineReader = defaultLineReader;
    }

}