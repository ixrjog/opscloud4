package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabUserDriver;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import lombok.RequiredArgsConstructor;
import org.gitlab.api.models.CreateUserRequest;
import org.gitlab.api.models.GitlabUser;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    public List<GitlabUser> findUser(GitlabConfig.Gitlab gitlab, String emailOrUsername) throws TicketProcessException {
        try {
            return GitlabUserDriver.findUser(gitlab, emailOrUsername);
        } catch (IOException e) {
            throw new TicketProcessException(String.format("Gitlab 查询用户错误: %s", e.getMessage()));
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public GitlabUser createGitlabUser(GitlabConfig.Gitlab gitlab, String username) throws TicketProcessException {
        User user = userService.getByUsername(username);
        CreateUserRequest request = new CreateUserRequest(user.getDisplayName(), username, user.getEmail());
        request.setPassword(stringEncryptor.decrypt(user.getPassword()));
        request.setResetPassword(false);
        request.setSkipConfirmation(true);
        try {
            return GitlabUserDriver.createUser(gitlab, request);
        } catch (IOException e) {
            throw new TicketProcessException(String.format("Gitlab 创建用户错误: %s", e.getMessage()));
        }
    }

}