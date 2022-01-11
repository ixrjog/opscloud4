package com.baiyi.opscloud.domain.vo.workorder;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/10/21 11:13 上午
 * @Version 1.0
 */
public class WorkflowVO {

    /**
     * workflow:
     * nodes:
     * - name: 上级
     * type: 0 #0用户指定 ， 1 符合条件群发， 2 系统指定
     * tags:
     * - TeamLeader
     * - name: 运维
     * type: 0 #0用户指定 ， 1 符合条件群发， 2 系统指定
     * tags:
     * - Operations
     */

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class WorkflowView {

        public static final WorkflowView EMPTY = WorkflowView.builder().build();

        @Builder.Default
        private List<Node> nodes = Lists.newArrayList();

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Node {

        @ApiModelProperty(value = "节点名称")
        private String name;
        @ApiModelProperty(value = "节点类型，参考NodeTypeConstants")
        @Builder.Default
        private Integer type = 0;
        @ApiModelProperty(value = "筛选用户的标签")
        private Set<String> tags;

    }

}
