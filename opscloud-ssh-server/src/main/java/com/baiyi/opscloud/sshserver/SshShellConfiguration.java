/*
 * Copyright (c) 2021 François Onimus
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.auth.pubkey.RejectAllPublickeyAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Ssh shell configuration
 */

@Slf4j
@Configuration
public class SshShellConfiguration {

    private final SshShellProperties properties;

    private final SshShellCommandFactory shellCommandFactory;

    private final PasswordAuthenticator passwordAuthenticator;

    private final PublickeyAuthenticator publickeyAuthenticator;

    public SshShellConfiguration(SshShellProperties properties,
                                 SshShellCommandFactory shellCommandFactory,
                                 PasswordAuthenticator passwordAuthenticator, PublickeyAuthenticator publickeyAuthenticator) {
        this.properties = properties;
        this.shellCommandFactory = shellCommandFactory;
        this.passwordAuthenticator = passwordAuthenticator;
        this.publickeyAuthenticator = publickeyAuthenticator;
    }

    /**
     * Create the bean responsible for starting and stopping the SSH server
     *
     * @param sshServer the ssh server to manage
     * @return ssh server lifecycle
     */
    @Bean
    public SshServerLifecycle sshServerLifecycle(SshServer sshServer) {
        return new SshServerLifecycle(sshServer, this.properties);
    }

    /**
     * Construct ssh server thanks to ssh shell properties
     *
     * @return ssh server
     */
    @Bean
    public SshServer sshServer() throws IOException {
        SshServer server = SshServer.setUpDefaultServer();
        server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(properties.getHostKeyFile().toPath()));
        server.setHost(properties.getHost());
        server.setPasswordAuthenticator(passwordAuthenticator);
        server.setPublickeyAuthenticator(RejectAllPublickeyAuthenticator.INSTANCE);
        if (properties.getAuthentication() == SshShellProperties.AuthenticationType.security) {
            server.setPublickeyAuthenticator(publickeyAuthenticator);
        }
        server.setPort(properties.getPort());
        server.setShellFactory(channelSession -> shellCommandFactory);
      //  server.setIoServiceFactoryFactory( e -> new Nio2ServiceFactoryFactory);
        server.setCommandFactory((channelSession, s) -> shellCommandFactory);
        return server;
    }

    @RequiredArgsConstructor
    public static class SshServerLifecycle {

        private final SshServer sshServer;

        private final SshShellProperties properties;

        /**
         * Start ssh server
         *
         * @throws IOException in case of error
         */
        @PostConstruct
        public void startServer() throws IOException {
            sshServer.start();
            log.info("Ssh server started [{}:{}]", properties.getHost(), properties.getPort());
        }

        /**
         * Stop ssh server
         *
         * @throws IOException in case of error
         */
        @PreDestroy
        public void stopServer() throws IOException {
            sshServer.stop();
        }
    }

}
