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

import com.baiyi.opscloud.sshcore.util.ChannelShellUtil;
import com.baiyi.opscloud.sshserver.auth.SshAuthentication;
import com.baiyi.opscloud.sshserver.auth.SshShellSecurityAuthenticationProvider;
import com.baiyi.opscloud.sshserver.listeners.SshShellListenerService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.Factory;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.Signal;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.channel.ChannelSessionAware;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.session.ServerSession;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;
import org.springframework.shell.Shell;
import org.springframework.shell.context.DefaultShellContext;
import org.springframework.shell.jline.InteractiveShellRunner;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.result.DefaultResultHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.baiyi.opscloud.sshserver.SshShellCommandFactory.SSH_THREAD_CONTEXT;

/**
 * Runnable for ssh shell session
 */
@Slf4j
@AllArgsConstructor
public class SshShellRunnable
        implements Factory<Command>, ChannelSessionAware, Runnable {

    private static final String SSH_ENV_COLUMNS = "COLUMNS";

    private static final String SSH_ENV_LINES = "LINES";

    private static final String SSH_ENV_TERM = "TERM";

    private final SshShellProperties properties;
    private final SshShellListenerService shellListenerService;
    private final Banner shellBanner;
    private final Shell shell;
    private final LineReader lineReader;
    private final PromptProvider promptProvider;
    private final Completer completer;
    private final Environment environment;
    private ChannelSession session;
    @Getter
    private final org.apache.sshd.server.Environment sshEnv;
    private final SshShellCommandFactory sshShellCommandFactory;
    private final InputStream is;
    @Getter
    private final OutputStream os;
    private final ExitCallback ec;

    /**
     * Run ssh session
     */
    @Override
    public void run() {
        log.debug("{}: running...", session.toString());
        TerminalBuilder terminalBuilder = TerminalBuilder.builder()
                .system(false)
                .jna(true)
                .dumb(true)
                .encoding(StandardCharsets.UTF_8)
                .streams(is, os);
        boolean sizeAvailable = false;
        sshEnv.getEnv().put("LANG", ChannelShellUtil.DEF_UNICODE);
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
             PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
             Terminal terminal = terminalBuilder.build()
        ) {

            try {
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

                if (properties.isDisplayBanner() && shellBanner != null) {
                    shellBanner.printBanner(environment, this.getClass(), ps);
                }

                DefaultResultHandler resultHandler = new DefaultResultHandler(terminal);
                resultHandler.handleResult(baos.toString(StandardCharsets.UTF_8));
                resultHandler.handleResult("Please type `help` to see available commands");

                LineReader reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .appName("Spring Ssh Shell")
                        .completer(completer)
                        .highlighter(lineReader.getHighlighter())
                        .parser(lineReader.getParser())
                        .build();

                // 删除不可见字符, 可能会卡顿
                // lineReader.unsetOpt(LineReader.Option.BRACKETED_PASTE);

                Object authenticationObject = session.getSession().getIoSession().getAttribute(
                        SshShellSecurityAuthenticationProvider.AUTHENTICATION_ATTRIBUTE);
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
                new InteractiveShellRunner(reader, promptProvider, shell, new DefaultShellContext()).run(null);
                shellListenerService.onSessionStopped(session);
                log.debug("{}: closing", session);
                quit(0);
            } catch (Throwable e) {
                shellListenerService.onSessionError(session);
                log.error("{}: unexpected exception", session, e);
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

    @Override
    public Command create() {
        return sshShellCommandFactory;
    }

}