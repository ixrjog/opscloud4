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
        } catch (IOException e) {
            // "{\"message\":{\"access_level\":[\"is not included in the list"
            if (e.getMessage().contains("is not included in the list"))
                throw new TicketProcessException("Gitlab新增项目成员错误: 不支持授权 %s 角色", accessLevel.name());
            throw new TicketProcessException("Gitlab新增项目成员错误: %s", e.getMessage());
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<GitlabProjectMember> getProjectMembers(GitlabConfig.Gitlab gitlab, Integer projectId) throws TicketProcessException {
        try {
            return GitlabProjectDriver.getProjectMembers(gitlab, projectId);
        } catch (IOException e) {
            throw new TicketProcessException("Gitlab查询项目成员错误: %s", e.getMessage());
        }
    }

    @Retryable(value = TicketProcessException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void updateProjectMember(GitlabConfig.Gitlab gitlab, Integer projectId, Integer userId, GitlabAccessLevel accessLevel) throws TicketProcessException {
        try {
            GitlabProjectDriver.updateProjectMember(gitlab, projectId, userId, accessLevel);
        } catch (IOException e) {
            throw new TicketProcessException("Gitlab更新项目成员错误: %s", e.getMessage());
        }
    }

}