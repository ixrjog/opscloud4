package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabGroupDriver;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabGroupMember;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/6/27 20:03
 * @Version 1.0
 */
@Component
public class GitlabGroupDelegate {

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void addGroupMember(GitlabConfig.Gitlab gitlab, Integer groupId, Integer userId, GitlabAccessLevel accessLevel) throws TicketProcessException {
        try {
            GitlabGroupDriver.addGroupMember(gitlab, groupId, userId, accessLevel);
        } catch (IOException ioException) {
            throw new TicketProcessException("Gitlab实例API错误: addGroupMember");
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<GitlabGroupMember> getGroupMembers(GitlabConfig.Gitlab gitlab, Integer groupId) throws TicketProcessException {
        try {
            return GitlabGroupDriver.getGroupMembers(gitlab, groupId);
        } catch (Exception e) {
            throw new TicketProcessException("Gitlab实例API错误: getGroupMember");
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void deleteGroupMember(GitlabConfig.Gitlab gitlab, Integer groupId, Integer userId) throws TicketProcessException {
        try {
            GitlabGroupDriver.deleteGroupMember(gitlab, groupId, userId);
        } catch (IOException ioException) {
            throw new TicketProcessException("Gitlab实例API错误: deleteGroupMember");
        }
    }
}
