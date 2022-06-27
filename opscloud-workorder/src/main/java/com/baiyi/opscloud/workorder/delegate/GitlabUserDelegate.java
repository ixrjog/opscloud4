package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabUserDriver;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import org.gitlab.api.models.GitlabUser;
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
public class GitlabUserDelegate {

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<GitlabUser> findUser(GitlabConfig.Gitlab gitlab, String emailOrUsername) throws TicketProcessException {
        try {
            return GitlabUserDriver.findUser(gitlab, emailOrUsername);
        } catch (IOException ioException) {
            throw new TicketProcessException("Gitlab实例API错误: findUser");
        }
    }

}