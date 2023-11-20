package com.baiyi.opscloud.leo.converter;

import com.baiyi.opscloud.common.util.time.AgoUtil;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Tag;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/10 09:23
 * @Version 1.0
 */
public class GitLabBranchConverter {

    private GitLabBranchConverter() {
    }

    public static LeoBuildVO.Option toOption(String label, List<LeoBuildVO.BranchOrTag> branches) {
        return LeoBuildVO.Option.builder()
                .label(label)
                .options(toOptions(branches))
                .build();
    }

    private static List<LeoBuildVO.Children> toOptions(List<LeoBuildVO.BranchOrTag> branches) {

        return branches.stream().map(e -> LeoBuildVO.Children.builder()
                .label(e.getName())
                .value(e.getName())
                .desc(StringUtils.isNotBlank(e.getCommitMessage()) ? e.getCommitMessage() : e.getCommit())
                .commitId(e.getCommit())
                .commitMessage(e.getCommitMessage())
                .commitWebUrl(e.getCommitWebUrl())
                .authorName(e.getAuthorName())
                .authorEmail(e.getAuthorEmail())
                .authoredDate(e.getAuthoredDate())
                .ago(e.getAgo())
                .build()).collect(Collectors.toList());
    }

    public static List<LeoBuildVO.BranchOrTag> toBranches(List<Branch> branches) {
        if (CollectionUtils.isEmpty(branches)) {
            return Lists.newArrayList();
        }
        return branches.stream().map(branch -> {
                    LeoBuildVO.BranchOrTag branchOrTag = LeoBuildVO.BranchOrTag.builder()
                            .name(branch.getName())
                            .commit(branch.getCommit().getId())
                            .commitMessage(branch.getCommit().getMessage())
                            .commitWebUrl(branch.getCommit().getWebUrl())
                            .authorName(branch.getCommit().getAuthorName())
                            .authorEmail(branch.getCommit().getAuthorEmail())
                            .authoredDate(branch.getCommit().getAuthoredDate())
                            .build();
                    branchOrTag.setAgo(AgoUtil.format(branchOrTag.getAgoTime()));
                    return branchOrTag;
                }
        ).collect(Collectors.toList());
    }

    public static List<LeoBuildVO.BranchOrTag> toTags(List<Tag> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return Lists.newArrayList();
        }
        return tags.stream().map(e ->
                LeoBuildVO.BranchOrTag.builder()
                        .name(e.getName())
                        .message(e.getMessage())
                        .commit(e.getCommit().getId())
                        .commitMessage(e.getCommit().getMessage())
                        .build()
        ).collect(Collectors.toList());
    }

}