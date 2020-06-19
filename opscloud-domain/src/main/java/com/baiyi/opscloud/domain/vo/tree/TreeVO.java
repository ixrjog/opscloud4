package com.baiyi.opscloud.domain.vo.tree;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/14 8:33 下午
 * @Version 1.0
 */
public class TreeVO {
    @Data
    @Builder
    @ApiModel
    public static class Tree {
        private String id;
        private String label;
        private Boolean disabled;
        private List<TreeVO.Tree> children;
    }

    @Data
    @Builder
    @ApiModel
    public static class DeptTree {
        private Integer id;
        private String label;
        private List<TreeVO.DeptTree> children;
    }
}
