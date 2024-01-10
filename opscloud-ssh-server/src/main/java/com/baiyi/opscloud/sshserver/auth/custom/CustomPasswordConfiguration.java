package com.baiyi.opscloud.sshserver.auth.custom;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.sshserver.auth.SshShellAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * {@code @Author} baiyi
 * {@code @Date} 2023/4/4 10:12
 * {@code @Version} 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomPasswordConfiguration {

    private final UserService userService;

    @Bean
    @Primary
    public SshShellAuthenticationProvider passwordAuthenticatorProvider() {
        return (username, pass, serverSession) -> {
            User user = userService.getByUsername(username);
            if (user == null) {
                return false;
            }
            return pass.equals(user.getPassword());
        };
    }

}