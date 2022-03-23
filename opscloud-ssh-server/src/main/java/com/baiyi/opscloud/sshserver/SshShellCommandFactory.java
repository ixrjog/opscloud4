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

import com.baiyi.opscloud.sshcore.model.SessionIdMapper;
import com.baiyi.opscloud.sshcore.model.SshIO;
import com.baiyi.opscloud.sshserver.listeners.SshShellListenerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.io.IoSession;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.session.ServerSession;
import org.jline.reader.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Ssh shell command implementation, which starts threads of SshShellRunnable
 *
 * @see SshShellRunnable
 */
@Slf4j
@Component
public class SshShellCommandFactory implements Command {

    public static final ThreadLocal<SshContext> SSH_THREAD_CONTEXT = ThreadLocal.withInitial(() -> null);

    private final SshShellProperties properties;

    private final SshShellListenerService shellListenerService;

    private final Banner shellBanner;

    private final PromptProvider promptProvider;

    private final Shell shell;

    private final JLineShellAutoConfiguration.CompleterAdapter completerAdapter;

    private final Parser parser;

    private final Environment environment;

    public static final ThreadLocal<SshIO> SSH_IO_CONTEXT = ThreadLocal.withInitial(SshIO::new);

    private final Map<ChannelSession, Thread> threads = new ConcurrentHashMap<>();

    /**
     * Constructor
     *
     * @param shellListenerService shell listener service
     * @param banner               shell banner
     * @param promptProvider       prompt provider
     * @param shell                spring shell
     * @param completerAdapter     completer adapter
     * @param parser               jline parser
     * @param environment          spring environment
     * @param properties           ssh shell properties
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SshShellCommandFactory(SshShellListenerService shellListenerService,
                                  @Autowired(required = false) Banner banner,
                                  @Lazy PromptProvider promptProvider,
                                  Shell shell,
                                  JLineShellAutoConfiguration.CompleterAdapter completerAdapter, Parser parser,
                                  Environment environment,
                                  SshShellProperties properties) {
        this.shellListenerService = shellListenerService;
        this.shellBanner = banner;
        this.promptProvider = promptProvider;
        this.shell = shell;
        this.completerAdapter = completerAdapter;
        this.parser = parser;
        this.environment = environment;
        this.properties = properties;
    }

    /**
     * Start ssh session
     *
     * @param channelSession ssh channel session
     * @param env            ssh environment
     */
    @Override
    public void start(ChannelSession channelSession, org.apache.sshd.server.Environment env) {
        SshIO sshIO = SSH_IO_CONTEXT.get();
        Thread sshThread = new Thread(new ThreadGroup("ssh-shell"),
                new SshShellRunnable(properties,
                channelSession, shellListenerService, shellBanner, promptProvider, shell, completerAdapter, parser,
                environment, env, this, sshIO.getIs(), sshIO.getOs(), sshIO.getEc()),
                "ssh-session-" + System.nanoTime());
        sshThread.start();
        threads.put(channelSession, sshThread);


        ServerSession serverSession = channelSession.getSession();
        IoSession ioSession = serverSession.getIoSession();
        SessionIdMapper.put(ioSession);
        log.debug("{}: started [{} session(s) currently active]", channelSession, threads.size());
    }

    @Override
    public void destroy(ChannelSession channelSession) {
        Thread sshThread = threads.remove(channelSession);
        if (sshThread != null) {
            sshThread.interrupt();
        }
        log.debug("{}: destroyed [{} session(s) currently active]", channelSession, threads.size());
    }

    @Override
    public void setErrorStream(OutputStream errOS) {
        // not used
    }

    @Override
    public void setExitCallback(ExitCallback ec) {
        SSH_IO_CONTEXT.get().setEc(ec);
    }

    @Override
    public void setInputStream(InputStream is) {
        SSH_IO_CONTEXT.get().setIs(is);
    }

    @Override
    public void setOutputStream(OutputStream os) {
        SSH_IO_CONTEXT.get().setOs(os);
    }

    public Map<Long, ChannelSession> listSessions() {
        return threads.keySet().stream()
                .collect(Collectors.toMap(s -> s.getServerSession().getIoSession().getId(), Function.identity()));
    }

}
