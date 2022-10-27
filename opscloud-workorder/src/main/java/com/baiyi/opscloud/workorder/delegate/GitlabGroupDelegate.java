package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.feature.GitLabGroupDriver;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/6/27 20:03
 * @Version 1.0
 */
@Component
public class GitlabGroupDelegate {

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void addMember(GitlabConfig.Gitlab gitlab, Long groupId, Long userId, AccessLevel accessLevel) throws TicketProcessException {
        try {
            GitLabGroupDriver.addMember(gitlab, groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            throw new TicketProcessException("GitLab新增群组成员错误: %s", e.getMessage());
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void updateMember(GitlabConfig.Gitlab gitlab, Long groupId, Long userId, AccessLevel accessLevel) throws TicketProcessException {
        try {
            GitLabGroupDriver.updateMember(gitlab, groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            throw new TicketProcessException("GitLab删除群组成员错误: %s", e.getMessage());
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<Member> getMembers(GitlabConfig.Gitlab gitlab, Integer groupId) throws TicketProcessException {
        try {
            return GitLabGroupDriver.getMembersWithGroupId(gitlab, groupId.longValue());
        } catch (GitLabApiException e) {
            throw new TicketProcessException("GitLab查询群组成员错误: %s", e.getMessage());
        }
    }


}
