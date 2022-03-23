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

import org.apache.sshd.server.SshServer;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.impl.history.DefaultHistory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

import static com.baiyi.opscloud.sshserver.SshShellProperties.SSH_SHELL_ENABLE;


/**
 * <p>Ssh shell auto configuration</p>
 * <p>Can be disabled by property <b>ssh.shell.enable=false</b></p>
 */
@Configuration
@ConditionalOnClass(SshServer.class)
@ConditionalOnProperty(name = SSH_SHELL_ENABLE, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SshShellProperties.class})
@AutoConfigureBefore({JLineShellAutoConfiguration.class, SpringShellAutoConfiguration.class})
public class SshShellHistoryAutoConfiguration {

    public static final String HISTORY_FILE = "historyFile";

    @Bean(HISTORY_FILE)
    public File historyFile(SshShellProperties properties) {
        return properties.getHistoryFile();
    }

    @Configuration
    public static class HistoryConfiguration {

        @Resource
        @Lazy
        private History history;

        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        @Bean
        @Primary
        public History history(LineReader lineReader, @Qualifier(HISTORY_FILE) File historyFile) {
            lineReader.setVariable(LineReader.HISTORY_FILE, historyFile.toPath());
            return new DefaultHistory(lineReader);
        }

        @EventListener
        public void onContextClosedEvent(ContextClosedEvent event) throws IOException {
            history.save();
        }
    }

}

