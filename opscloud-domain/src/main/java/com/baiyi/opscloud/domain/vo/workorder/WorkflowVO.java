package com.baiyi.opscloud.domain.vo.workorder;

import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/21 11:13 上午
 * @Version 1.0
 */
public class WorkflowVO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class WorkflowView implements Serializable {
        private static final long serialVersionUID = -11415446570849511L;
        @Builder.Default
        private List<NodeView> nodes = Lists.newArrayList();
    }

    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class NodeView extends Node implements Serializable {
        private static final long serialVersionUID = -5431171001287996887L;
        private List<UserVO.User> auditUsers;
        private UserVO.User auditUser;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Workflow implements Serializable {
        public static final Workflow EMPTY_WORKFLOW = Workflow.builder().build();
        private static final long serialVersionUID = -837044550263261422L;
        @Builder.Default
        private List<Node> nodes = Lists.newArrayList();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Node implements Serializable {
        private static final long serialVersionUID = 2636039751664799398L;
        @ApiModelProperty(value = "节点名称")
        private String name;
        @ApiModelProperty(value = "节点类型，参考NodeTypeConstants")
        @Builder.Default
        private Integer type = 0;
        private String comment;
        @ApiModelProperty(value = "筛选用户的标签")
        private List<String> tags;
    }

}
