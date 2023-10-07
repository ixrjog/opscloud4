package com.baiyi.opscloud.datasource.gitlab.driver;

import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.client.GitLabApiBuilder;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.RepositoryFile;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/4/24 09:42
 * @Version 1.0
 */
@Slf4j
public class GitLabRepositoryDriver {

    /**
     * 从项目分支获取文件
     *
     * @param gitlab
     * @param projectId
     * @param filePath
     * @param ref
     * @return
     * @throws GitLabApiException
     */
    public static RepositoryFile getRepositoryFile(GitLabConfig.GitLab gitlab, Long projectId, String filePath, String ref) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getRepositoryFileApi().getFile(projectId, filePath, ref);
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static Optional<RepositoryFile> getRepositoryOptionalFile(GitLabConfig.GitLab gitlab, Long projectId, String filePath, String ref) {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getRepositoryFileApi().getOptionalFileInfo(projectId, filePath, ref);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

}
