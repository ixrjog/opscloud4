package com.baiyi.opscloud.domain.vo.leo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/9 17:46
 * @Version 1.0
 */
public class LeoBuildVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class BranchOptions implements Serializable {

        private static final long serialVersionUID = 6999931515242829844L;

        public static final BranchOptions EMPTY_OPTIONS = BranchOptions.builder().build();

        @Builder.Default
        private List<Option> options = Lists.newArrayList();
    }

    @Data
    @Builder
    @ApiModel
    public static class Option implements Serializable {

        private static final long serialVersionUID = -6482052319954322970L;

        private String label;
        private List<Children> options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class Children implements Serializable {

        private static final long serialVersionUID = -1561200881442892379L;

        private String value;
        private String label;

        private String desc;

        private String commitId;
        private String commitMessage;
    }

    @Data
    @Builder
    @ApiModel
    @AllArgsConstructor
    public static class BranchOrTag implements Serializable {

        private static final long serialVersionUID = 8033652452171334565L;

        private String name;
        private String message;
        private String commit;
        private String commitMessage;
        private String commitUrl;

    }

}
