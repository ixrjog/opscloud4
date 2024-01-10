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

package com.baiyi.opscloud.sshserver.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.channel.ChannelSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Service calling listeners
 */
@Slf4j
public class SshShellListenerService {

    private final List<SshShellListener> listeners;

    public SshShellListenerService(List<SshShellListener> listeners) {
        this.listeners = listeners == null ? new ArrayList<>() : listeners;
        log.info("Ssh shell listener service initialized with {} listeners", this.listeners.size());
    }

    /**
     * Session started
     *
     * @param channelSession ssh channel session
     */
    public void onSessionStarted(ChannelSession channelSession) {
        notify(new SshShellEvent(SshShellEventType.SESSION_STARTED, channelSession));
    }

    /**
     * Session stopped
     *
     * @param channelSession ssh channel session
     */
    public void onSessionStopped(ChannelSession channelSession) {
        notify(new SshShellEvent(SshShellEventType.SESSION_STOPPED, channelSession));
    }

    /**
     * Session destroyed
     *
     * @param channelSession ssh channel session
     */
    public void onSessionDestroyed(ChannelSession channelSession) {
        notify(new SshShellEvent(SshShellEventType.SESSION_DESTROYED, channelSession));
    }

    /**
     * Session stopped with error
     *
     * @param channelSession ssh channel session
     */
    public void onSessionError(ChannelSession channelSession) {
        notify(new SshShellEvent(SshShellEventType.SESSION_STOPPED_UNEXPECTEDLY, channelSession));
    }

    private void notify(SshShellEvent event) {
        this.listeners.forEach(listener -> {
            try {
                listener.onEvent(event);
            } catch (RuntimeException e) {
                log.error("Unable to execute onSessionStarted on listener : {}", listener.getClass().getName(), e);
            }
        });
    }

}