package com.baiyi.opscloud.workorder.delegate;

import com.baiyi.opscloud.common.datasource.GitlabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitlabProjectDriver;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabProjectMember;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/6/27 17:37
 * @Version 1.0
 */
@Component
public class GitlabProjectDelegate {

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void addProjectMember(GitlabConfig.Gitlab gitlab, Integer projectId, Integer userId, GitlabAccessLevel accessLevel) throws TicketProcessException {
        try {
            GitlabProjectDriver.addProjectMember(gitlab, projectId, userId, accessLevel);
        } catch (IOException ioException) {
            throw new TicketProcessException("Gitlab实例API错误: addProjectMember");
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<GitlabProjectMember> getProjectMembers(GitlabConfig.Gitlab gitlab, Integer projectId) throws TicketProcessException {
        try {
            return GitlabProjectDriver.getProjectMembers(gitlab, projectId);
        } catch (IOException ioException) {
            throw new TicketProcessException("Gitlab实例API错误: getProjectMembers");
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void updateProjectMember(GitlabConfig.Gitlab gitlab, Integer projectId, Integer userId, GitlabAccessLevel accessLevel) throws TicketProcessException {
        try {
            GitlabProjectDriver.updateProjectMember(gitlab, projectId, userId, accessLevel);
        } catch (IOException ioException) {
            throw new TicketProcessException("Gitlab实例API错误: updateProjectMember");
        }
    }

}