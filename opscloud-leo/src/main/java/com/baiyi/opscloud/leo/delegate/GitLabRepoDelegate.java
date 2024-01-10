package com.baiyi.opscloud.leo.delegate;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.GitLabConfig;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabProjectDriver;
import com.baiyi.opscloud.datasource.gitlab.driver.GitLabRepositoryDriver;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.converter.CompareResultsConverter;
import com.baiyi.opscloud.leo.converter.GitLabBranchConverter;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Tag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
     * 默认创建分支（发布分支）
     */
    public static final List<String> DEF_BRANCHES = Lists.newArrayList("dev", "daily", "sit", "pre", "master");

    /**
     * 生成GitLab分支选项
     *
     * @param gitlab
     * @param projectId
     * @param openTag
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10S, key = "'URL:' + #gitlab.url + '&PID:' + #projectId + '&TAG:' + #openTag")
    public LeoBuildVO.BranchOptions generatorGitLabBranchOptions(GitLabConfig.GitLab gitlab, Long projectId, boolean openTag) {
        List<LeoBuildVO.Option> options = Lists.newArrayList();
        try {
            // Branches option
            List<Branch> gitLabBranches = GitLabProjectDriver.getBranchesWithProjectId(gitlab, projectId);
            List<LeoBuildVO.BranchOrTag> branches = GitLabBranchConverter.toBranches(gitLabBranches);
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
            log.warn("Query gitLab branches or tags: url={}, projectId={}, {}", gitlab.getUrl(), projectId, e.getMessage());
            return LeoBuildVO.BranchOptions.EMPTY_OPTIONS;
        }
    }

    @SuppressWarnings("SpringCacheableMethodCallsInspection")
    public LeoBuildVO.BranchOptions createGitLabBranch(GitLabConfig.GitLab gitlab, Long projectId, String ref) {
        for (String branch : DEF_BRANCHES) {
            try {
                GitLabProjectDriver.createBranch(gitlab, projectId, branch, ref);
            } catch (GitLabApiException e) {
                log.warn("Create gitLab branch err: url={}, projectId={}, branch={}, ref={}, {}", gitlab.getUrl(), projectId, branch, ref, e.getMessage());
            }
        }
        // 忽略缓存
        return generatorGitLabBranchOptions(gitlab, projectId, false);
    }

    public Commit getBranchOrTagCommit(GitLabConfig.GitLab gitlab, Long projectId, String branchNameOrTagName) {
        try {
            Optional<Branch> optionalBranch = GitLabProjectDriver.getBranchWithProjectIdAndBranchName(gitlab, projectId, branchNameOrTagName);
            if (optionalBranch.isPresent()) {
                return optionalBranch.get().getCommit();
            }

            Optional<Tag> optionalTag = GitLabProjectDriver.getTagWithProjectIdAndTagName(gitlab, projectId, branchNameOrTagName);
            if (optionalTag.isPresent()) {
                return optionalTag.get().getCommit();
            }
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
        }
        throw new LeoBuildException("Query build branch commit err: gitLab={}, projectId={}, branchOrTag={}", gitlab.getUrl(), projectId, branchNameOrTagName);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10S, key = "'URL:' + #gitlab.url + '&PID:' + #projectId + '&FROM:' + #from + '&TO:' + #to", unless = "#result == null")
    public LeoBuildVO.CompareResults compareBranch(GitLabConfig.GitLab gitlab, Long projectId, String from, String to) {
        try {
            return CompareResultsConverter.to(GitLabRepositoryDriver.compare(gitlab, projectId, from, to));
        } catch (GitLabApiException e) {
            return LeoBuildVO.CompareResults.builder()
                    .success(false)
                    .msg(e.getMessage())
                    .build();
        }
    }

}