package com.baiyi.opscloud.datasource.jenkins.status;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/1 10:06
 * @Version 1.0
 */
public class JenkinsBuildExecutorStatusVO implements Serializable {

    private static final long serialVersionUID = -2642546999291350826L;

    @Data
    @Builder
    @ApiModel
    public static class Children implements Serializable {

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
