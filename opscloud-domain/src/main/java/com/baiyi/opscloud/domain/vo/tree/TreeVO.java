package com.baiyi.opscloud.domain.vo.tree;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
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
    public static class Tree implements Serializable {
        private static final long serialVersionUID = 8188509874837434759L;
        private String id;
        private String label;
        private Object value;
        private Boolean disabled;
        private List<TreeVO.Tree> children;
    }

    @Data
    @Builder
    @ApiModel
    public static class DeptTree implements Serializable{
        private static final long serialVersionUID = 2763890334936283925L;
        private Integer id;
        private String label;
        private List<TreeVO.DeptTree> children;
    }
}
