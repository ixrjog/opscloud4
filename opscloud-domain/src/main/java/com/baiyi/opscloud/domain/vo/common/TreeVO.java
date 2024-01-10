package com.baiyi.opscloud.domain.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author 修远
 * @Date 2021/6/3 11:18 上午
 * @Since 1.0
 */
public class TreeVO {

    @Data
    @Builder
    @Schema
    public static class Tree implements Serializable {
        @Serial
        private static final long serialVersionUID = 8188509874837434759L;
        private String id;
        private String label;
        private Object value;
        private Boolean disabled;
        private List<Tree> children;
    }

    @Data
    @Builder
    @Schema
    public static class DeptTree implements Serializable {
        @Serial
        private static final long serialVersionUID = 2763890334936283925L;
        private Integer id;
        private String label;
        private List<TreeVO.DeptTree> children;
    }

}