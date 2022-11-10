package com.baiyi.opscloud.leo.delegate;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabProjectDriver;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.converter.GitLabBranchConverter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Tag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/9 20:39
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabRepoDelegate {

    public static final String BRANCHES = "Branches";
    public static final String TAGS = "Tags";

    /**
     * 生成GitLab分支选项
     *
     * @param gitlab
     * @param projectId
     * @param openTag
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10S, key = "'url_' + #gitlab.url + '_projectId_' + #projectId + '_openTag_' + #openTag")
    public LeoBuildVO.BranchOptions generatorGitLabBranchOptions(GitLabConfig.Gitlab gitlab, Long projectId, boolean openTag) {
        List<LeoBuildVO.Option> options = Lists.newArrayList();
        try {
            // Branches option
            List<Branch> GitLabBranches = GitLabProjectDriver.getBranchesWithProjectId(gitlab, projectId);
            List<LeoBuildVO.BranchOrTag> branches = GitLabBranchConverter.toBranches(GitLabBranches);
            options.add(GitLabBranchConverter.toOption(BRANCHES, branches));
            // Tags option
            if (openTag) {
                List<Tag> gitLabTags = GitLabProjectDriver.getTagsWithProjectId(gitlab, projectId);
                List<LeoBuildVO.BranchOrTag> tags = GitLabBranchConverter.toTags(gitLabTags);
                options.add(GitLabBranchConverter.toOption(TAGS, tags));
            }
            return LeoBuildVO.BranchOptions.builder()
                    .options(options)
                    .build();
        } catch (GitLabApiException e) {
            log.warn("查询GitLab branches tags: url={}, projectId={}, err={}", gitlab.getUrl(), projectId, e.getMessage());
            return LeoBuildVO.BranchOptions.EMPTY_OPTIONS;
        }
    }

}
