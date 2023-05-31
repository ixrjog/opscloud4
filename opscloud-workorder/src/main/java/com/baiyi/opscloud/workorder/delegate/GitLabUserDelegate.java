package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabUserDriver;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/6/27 17:42
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GitLabUserDelegate {

    private final UserService userService;

    private final StringEncryptor stringEncryptor;

    @Retryable(retryFor = TicketProcessException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<User> findUsers(GitLabConfig.GitLab gitlab, String emailOrUsername) throws TicketProcessException {
        try {
            return GitLabUserDriver.findUsers(gitlab, emailOrUsername);
        } catch (GitLabApiException e) {
            throw new TicketProcessException("GitLab查询用户错误: {}", e.getMessage());
        }
    }

    @Retryable(retryFor = TicketProcessException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public User createUser(GitLabConfig.GitLab gitlab, String username) throws TicketProcessException {
        com.baiyi.opscloud.domain.generator.opscloud.User ocUser = userService.getByUsername(username);
        User user = new User()
                .withUsername(username)
                .withName(ocUser.getDisplayName())
                .withEmail(ocUser.getEmail())
                // 跳过确认
                .withSkipConfirmation(true);
        try {
            return GitLabUserDriver.createUser(gitlab, user, stringEncryptor.decrypt(ocUser.getPassword()));
        } catch (GitLabApiException e) {
            throw new TicketProcessException("GitLab创建用户错误: {}", e.getMessage());
        }
    }

}