package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabProjectDriver;
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
 * @Date 2022/6/27 17:37
 * @Version 1.0
 */
@Component
public class GitLabProjectDelegate {

    @Retryable(retryFor = TicketProcessException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void addProjectMember(GitLabConfig.GitLab gitlab, Long projectId, Long userId, AccessLevel accessLevel) throws TicketProcessException {
        try {
            GitLabProjectDriver.addMember(gitlab, projectId, userId, accessLevel);
        } catch (GitLabApiException e) {
            //   "{"message":{"access_level":["is not included in the list"
            if (e.getMessage().contains("is not included in the list")) {
                throw new TicketProcessException("Gitlab新增项目成员错误: 不支持授权 {} 角色", accessLevel.name());
            }
            throw new TicketProcessException("GitLab新增项目成员错误: {}", e.getMessage());
        }
    }

    /**
     * 新API
     *
     * @param gitlab
     * @param projectId
     * @return
     * @throws TicketProcessException
     */
    @Retryable(retryFor = TicketProcessException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<Member> getProjectMembers(GitLabConfig.GitLab gitlab, Long projectId) throws TicketProcessException {
        try {
            return GitLabProjectDriver.getMembersWithProjectId(gitlab, projectId);
        } catch (GitLabApiException e) {
            throw new TicketProcessException("GitLab查询项目成员错误: {}", e.getMessage());
        }
    }

    @Retryable(retryFor = TicketProcessException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void updateProjectMember(GitLabConfig.GitLab gitlab, Long projectId, Long userId, AccessLevel accessLevel) throws TicketProcessException {
        try {
            GitLabProjectDriver.updateMember(gitlab, projectId, userId, accessLevel);
        } catch (GitLabApiException e) {
            throw new TicketProcessException("GitLab更新项目成员错误: {}", e.getMessage());
        }
    }

}