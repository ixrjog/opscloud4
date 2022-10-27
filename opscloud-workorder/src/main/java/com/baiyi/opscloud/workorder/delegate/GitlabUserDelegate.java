package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
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
public class GitlabUserDelegate {

    private final UserService userService;

    private final StringEncryptor stringEncryptor;

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<User> findUser(GitlabConfig.Gitlab gitlab, String emailOrUsername) throws TicketProcessException {
        try {
            return GitLabUserDriver.findUsers(gitlab, emailOrUsername);
        } catch (GitLabApiException e) {
            throw new TicketProcessException("GitLab查询用户错误: %s", e.getMessage());
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public User createGitlabUser(GitlabConfig.Gitlab gitlab, String username) throws TicketProcessException {
        com.baiyi.opscloud.domain.generator.opscloud.User ocUser = userService.getByUsername(username);
        User user = new User()
                .withUsername(username)
                .withName(ocUser.getDisplayName())
                .withEmail(ocUser.getEmail())
                .withSkipConfirmation(true);
//        CreateUserRequest request = new CreateUserRequest(ocUser.getDisplayName(), username, ocUser.getEmail());
//        request.setPassword(stringEncryptor.decrypt(ocUser.getPassword()));
//        request.setResetPassword(false);
//        request.setSkipConfirmation(true);
        try {
            return GitLabUserDriver.createUser(gitlab, user,stringEncryptor.decrypt(ocUser.getPassword()));
        } catch (GitLabApiException e) {
            throw new TicketProcessException("Gitlab创建用户错误: %s", e.getMessage());
        }
    }

}