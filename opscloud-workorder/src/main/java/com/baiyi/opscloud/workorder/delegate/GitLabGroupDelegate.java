package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabGroupDriver;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class GitLabGroupDelegate {

    @Retryable(retryFor = TicketProcessException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void addMember(GitLabConfig.GitLab gitlab, Long groupId, Long userId, AccessLevel accessLevel) throws TicketProcessException {
        try {
            GitLabGroupDriver.addMember(gitlab, groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            log.error("GitLab新增群组成员错误: {}", e.getMessage());
            throw new TicketProcessException("GitLab新增群组成员错误: {}", e.getMessage());
        }
    }

    @Retryable(retryFor = TicketProcessException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void updateMember(GitLabConfig.GitLab gitlab, Long groupId, Long userId, AccessLevel accessLevel) throws TicketProcessException {
        try {
            GitLabGroupDriver.updateMember(gitlab, groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            log.error("GitLab删除群组成员错误: {}", e.getMessage());
            throw new TicketProcessException("GitLab删除群组成员错误: {}", e.getMessage());
        }
    }

    @Retryable(retryFor = TicketProcessException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<Member> getMembers(GitLabConfig.GitLab gitlab, Integer groupId) throws TicketProcessException {
        try {
            return GitLabGroupDriver.getMembersWithGroupId(gitlab, groupId.longValue());
        } catch (GitLabApiException e) {
            log.error("GitLab查询群组成员错误: {}", e.getMessage());
            throw new TicketProcessException("GitLab查询群组成员错误: {}", e.getMessage());
        }
    }

}