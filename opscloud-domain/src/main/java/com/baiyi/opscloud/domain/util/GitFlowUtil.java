package com.baiyi.opscloud.domain.util;

import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/6/19 20:29
 * @Version 1.0
 */
public class GitFlowUtil {

    private static final String ALL = "*";

    public static final String BRANCHES = "Branches";

    public static LeoBuildVO.BranchOptions filter(LeoBuildVO.BranchOptions branchOptions, List<String> envFilter) {
        // * 匹配所有
        if (envFilter.stream().anyMatch(ALL::equals)) {
            return branchOptions;
        }
        for (LeoBuildVO.Option option : branchOptions.getOptions()) {
            if (option.getLabel().equals(BRANCHES)) {
                option.setOptions(option.getOptions()
                        .stream()
                        .filter(o -> filter(o, envFilter))
                        .collect(Collectors.toList()));
            }
        }
        return branchOptions;
    }

    private static boolean filter(LeoBuildVO.Children option, List<String> envFilter) {
        for (String filterStr : envFilter) {
            // 前缀模糊匹配
            if (filterStr.endsWith(ALL)) {
                String prefix = filterStr.replace(ALL, "");
                if (option.getValue().startsWith(prefix)) {
                    return true;
                }
            } else {
                if (option.getValue().equals(filterStr)) {
                    return true;
                }
            }
        }
        return false;
    }

}