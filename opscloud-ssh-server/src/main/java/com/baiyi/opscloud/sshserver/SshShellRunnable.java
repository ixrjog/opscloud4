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
import com.baiyi.opscloud.sshserver.listeners.SshShellListenerService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.Factory;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.Signal;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.channel.ChannelSessionAware;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.session.ServerSession;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;
import org.springframework.shell.ExitRequest;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.result.DefaultResultHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.baiyi.opscloud.sshserver.SshShellCommandFactory.SSH_THREAD_CONTEXT;
import static com.baiyi.opscloud.sshserver.auth.SshShellAuthenticationProvider.AUTHENTICATION_ATTRIBUTE;


/**
 * Runnable for ssh shell session
 */
@Slf4j
public class SshShellRunnable
        implements Factory<Command>, ChannelSessionAware, Runnable {

    private static final String SSH_ENV_COLUMNS = "COLUMNS";

    private static final String SSH_ENV_LINES = "LINES";

    private static final String SSH_ENV_TERM = "TERM";

    private final SshShellProperties properties;

    private ChannelSession session;

    private final SshShellListenerService shellListenerService;

    private final Banner shellBanner;

    private final PromptProvider promptProvider;

    private final Shell shell;

    private final JLineShellAutoConfiguration.CompleterAdapter completerAdapter;

    private final Parser parser;

    private final Environment environment;

    private final org.apache.sshd.server.Environment sshEnv;

    private final SshShellCommandFactory sshShellCommandFactory;

    private final InputStream is;

    @Getter
    private final OutputStream os;

    private final ExitCallback ec;

    public SshShellRunnable(SshShellProperties properties, ChannelSession session,
                            SshShellListenerService shellListenerService, Banner shellBanner,
                            PromptProvider promptProvider, Shell shell,
                            JLineShellAutoConfiguration.CompleterAdapter completerAdapter, Parser parser,
                            Environment environment, org.apache.sshd.server.Environment sshEnv,
                            SshShellCommandFactory sshShellCommandFactory, InputStream is,
                            OutputStream os, ExitCallback ec) {
        this.properties = properties;
        this.session = session;
        this.shellListenerService = shellListenerService;
        this.shellBanner = shellBanner;
        this.promptProvider = promptProvider;
        this.shell = shell;
        this.completerAdapter = completerAdapter;
        this.parser = parser;
        this.environment = environment;
        this.sshEnv = sshEnv;
        this.sshShellCommandFactory = sshShellCommandFactory;
        this.is = is;
        this.os = os;
        this.ec = ec;
    }

    /**
     * Run ssh session
     */
    @Override
    public void run() {
        log.debug("{}: running...", session.toString());
        TerminalBuilder terminalBuilder = TerminalBuilder.builder().system(false).streams(is, os);
        /**
         * 可以不加
         * TerminalBuilder terminalBuilder = TerminalBuilder.builder().system(false).streams(is, os)
         *    .dumb(true)
         *    .encoding(StandardCharsets.UTF_8.name());
         */
        boolean sizeAvailable = false;
        if (sshEnv.getEnv().containsKey(SSH_ENV_COLUMNS) && sshEnv.getEnv().containsKey(SSH_ENV_LINES)) {
            try {
                terminalBuilder.size(new Size(
                        Integer.parseInt(sshEnv.getEnv().get(SSH_ENV_COLUMNS)),
                        Integer.parseInt(sshEnv.getEnv().get(SSH_ENV_LINES))
                ));
                sizeAvailable = true;
            } catch (NumberFormatException e) {
                if (!log.isTraceEnabled()) {
                    log.debug("Unable to get terminal size : {}:{}", e.getClass().getSimpleName(), e.getMessage());
                } else {
                    log.trace("Unable to get terminal size", e);
                }
            }
        }
        if (sshEnv.getEnv().containsKey(SSH_ENV_LINES)) {
            terminalBuilder.type(sshEnv.getEnv().get(SSH_ENV_TERM));
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name());
             Terminal terminal = terminalBuilder.build()) {
            try {
                DefaultResultHandler resultHandler = new DefaultResultHandler();
                resultHandler.setTerminal(terminal);

                Attributes attr = terminal.getAttributes();
                SshShellUtils.fill(attr, sshEnv.getPtyModes());
                terminal.setAttributes(attr);

                if (sizeAvailable) {
                    sshEnv.addSignalListener((channel, signal) -> {
                        terminal.setSize(new Size(
                                Integer.parseInt(sshEnv.getEnv().get("COLUMNS")),
                                Integer.parseInt(sshEnv.getEnv().get("LINES"))));
                        terminal.raise(Terminal.Signal.WINCH);
                    }, Signal.WINCH);
                }
                // 清理屏幕
                //terminal.puts(InfoCmp.Capability.clear_screen, new Object[0]);
                terminal.puts(InfoCmp.Capability.clear_screen);
                if (properties.isDisplayBanner() && shellBanner != null) {
                    shellBanner.printBanner(environment, this.getClass(), ps);
                }
                resultHandler.handleResult(new String(baos.toByteArray(), StandardCharsets.UTF_8));
                resultHandler.handleResult("Please type `help` to see available commands");

                LineReader reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .appName("Spring Ssh Shell")
                        .completer(completerAdapter)
                        .highlighter((reader1, buffer) -> {
                            int l = 0;
                            String best = null;
                            for (String command : shell.listCommands().keySet()) {
                                if (buffer.startsWith(command) && command.length() > l) {
                                    l = command.length();
                                    best = command;
                                }
                            }
                            if (best != null) {
                                return new AttributedStringBuilder(buffer.length()).append(best,
                                                AttributedStyle.BOLD).append(buffer.substring(l))
                                        .toAttributedString();
                            } else {
                                return new AttributedString(buffer,
                                        AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
                            }
                        })
                        .parser(parser)
                        .build();

                Object authenticationObject = session.getSession().getIoSession().getAttribute(AUTHENTICATION_ATTRIBUTE);
                SshAuthentication authentication = null;
                if (authenticationObject != null) {
                    if (!(authenticationObject instanceof SshAuthentication)) {
                        throw new IllegalStateException("Unknown authentication object class: " + authenticationObject.getClass().getName());
                    }
                    authentication = (SshAuthentication) authenticationObject;
                }

                File historyFile = properties.getHistoryFile();
                if (!properties.isSharedHistory()) {
                    String user = authentication != null ? authentication.getName() : "unknown";
                    historyFile = new File(properties.getHistoryDirectory(), "sshShellHistory-" + user + ".log");
                }
                reader.setVariable(LineReader.HISTORY_FILE, historyFile.toPath());

                SSH_THREAD_CONTEXT.set(new SshContext(this, terminal, reader, authentication));
                shellListenerService.onSessionStarted(session);
                shell.run(new SshShellInputProvider(reader, promptProvider));
                shellListenerService.onSessionStopped(session);
                log.debug("{}: closing", session.toString());
                quit(0);
            } catch (Throwable e) {
                shellListenerService.onSessionError(session);
                log.error("{}: unexpected exception", session.toString(), e);
                quit(1);
            }
        } catch (IOException e) {
            log.error("Unable to open terminal", e);
            quit(1);
        }
    }

    private void quit(int exitCode) {
        if (ec != null) {
            ec.onExit(exitCode);
        }
    }

    @Override
    public void setChannelSession(ChannelSession session) {
        this.session = session;
    }

    public ServerSession getSshSession() {
        return session.getSession();
    }

    public org.apache.sshd.server.Environment getSshEnv() {
        return sshEnv;
    }

    @Override
    public Command create() {
        return sshShellCommandFactory;
    }

    static class SshShellInputProvider
            extends InteractiveShellApplicationRunner.JLineInputProvider {

        public SshShellInputProvider(LineReader lineReader, PromptProvider promptProvider) {
            super(lineReader, promptProvider);
        }

        @Override
        public Input readInput() {
            SshContext ctx = SSH_THREAD_CONTEXT.get();
            if (ctx != null) {
                ctx.getPostProcessorsList().clear();
            }
            try {
                return super.readInput();
            } catch (EndOfFileException e) {
                throw new ExitRequest(1);
            }
        }
    }
}
