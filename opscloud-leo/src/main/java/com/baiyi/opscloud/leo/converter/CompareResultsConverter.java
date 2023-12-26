package com.baiyi.opscloud.leo.converter;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import org.gitlab4j.api.models.CompareResults;

import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/11/16 10:46
 * @Version 1.0
 */
public class CompareResultsConverter {

    private CompareResultsConverter() {
    }

    public static LeoBuildVO.CompareResults to(CompareResults compareResults) {
        return LeoBuildVO.CompareResults.builder()
                .commits(compareResults.getCommits().stream().map(CompareResultsConverter::to).collect(Collectors.toList()))
                .commit(to(compareResults.getCommit()))
                .compareSameRef(compareResults.getCompareSameRef())
                .compareTimeout(compareResults.getCompareTimeout())
                .success(true)
                .build();
    }

    private static LeoBuildVO.Commit to(org.gitlab4j.api.models.Commit commit) {
        return BeanCopierUtil.copyProperties(commit, LeoBuildVO.Commit.class);
    }

}