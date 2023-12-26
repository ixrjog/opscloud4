package com.baiyi.opscloud.datasource.jenkins.status;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/1 10:06
 * @Version 1.0
 */
public class JenkinsBuildExecutorStatusVO {

    @Data
    @Builder
    @Schema
    public static class Children implements Serializable {

        @Serial
        private static final long serialVersionUID = 2615134095285978523L;

        private String name;

        @Builder.Default
        private Integer value = 0;

        @Builder.Default
        private List<Children> children = Lists.newArrayList();

        public void addChildren(Children children) {
            this.children.add(children);
        }

    }

}