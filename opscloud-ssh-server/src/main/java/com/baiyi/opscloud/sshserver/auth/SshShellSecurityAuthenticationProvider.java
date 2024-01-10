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

package com.baiyi.opscloud.sshserver.auth;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.sshserver.SshShellProperties.SSH_SHELL_PREFIX;

/**
 * Spring security ssh shell authentication provider
 */
@Slf4j
public class SshShellSecurityAuthenticationProvider
        implements SshShellAuthenticationProvider {

    private final ApplicationContext context;

    private final String authProviderBeanName;

    private AuthenticationManager authenticationManager;

    public SshShellSecurityAuthenticationProvider(ApplicationContext context, String authProviderBeanName) {
        this.context = context;
        this.authProviderBeanName = authProviderBeanName;
    }

    /**
     * Init security provider
     */
    @PostConstruct
    public void init() {
        Map<String, AuthenticationManager> map = context.getBeansOfType(AuthenticationManager.class);
        if (map.isEmpty()) {
            throw new BeanCreationException(
                    "Could not find any beans of class: " + AuthenticationManager.class.getName());
        }
        String beanName = authProviderBeanName;
        Set<String> available = map.keySet();
        if (beanName != null && !beanName.isEmpty()) {
            this.authenticationManager = map.get(beanName);
            if (this.authenticationManager == null) {
                throw new BeanCreationException(
                        "Could not find bean with name: " + beanName + " and class: " + AuthenticationManager.class
                                .getName() + ". Available are: "
                                + available);
            }
        } else {
            if (map.size() != 1) {
                throw new BeanCreationException(
                        "Found too many beans of class: " + AuthenticationManager.class.getName() + ". Please specify" +
                                " name with property '" + SSH_SHELL_PREFIX
                                + ".authProviderBeanName'");
            }
            Map.Entry<String, AuthenticationManager> e = map.entrySet().iterator().next();
            beanName = e.getKey();
            this.authenticationManager = e.getValue();
        }
        log.info("Using authentication manager named: {} [class={}]", beanName,
                this.authenticationManager.getClass().getName());
    }

    @Override
    public boolean authenticate(String username, String pass,
                                ServerSession serverSession) throws PasswordChangeRequiredException {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, pass));
            log.debug("User {} authenticated with authorities: {}", username, auth.getAuthorities());
            List<String> authorities =
                    auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            serverSession.getIoSession().setAttribute(AUTHENTICATION_ATTRIBUTE, new SshAuthentication(username,
                    auth.getPrincipal(), auth.getDetails(), auth.getCredentials(), authorities));
            return auth.isAuthenticated();
        } catch (AuthenticationException e) {
            log.error("Unable to authenticate user [{}] : {}", username, e.getMessage());
            log.debug("Unable to authenticate user [{}]", username, e);
            return false;
        }
    }

}