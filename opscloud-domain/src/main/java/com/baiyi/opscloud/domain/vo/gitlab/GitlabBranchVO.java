package com.baiyi.opscloud.domain.vo.gitlab;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/30 12:47 下午
 * @Version 1.0
 */
public class GitlabBranchVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Repository {
        private List<Option> options;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Option {
        private String label;
        private List<Children> options;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Children {
        private String value;
        private String label;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Branch extends BaseBranch {
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Tag extends BaseBranch {
    }

    @Data
    @ApiModel
    public static class BaseBranch {
        private String name;
        private String message;
        private String commit;
        private String commitMessage;
        private String commitUrl;
    }

}
