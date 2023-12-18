package com.baiyi.opscloud.domain.vo.server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/7 2:58 下午
 * @Version 1.0
 */
public class ServerTreeVO {

    @Data
    @Builder
    @Schema
    public static class ServerTree implements Serializable {
        @Serial
        private static final long serialVersionUID = 5811239982201037450L;
        private Integer size;
        private Integer userId;
        private String uuid;
        private List<Tree> tree;
    }

    @Data
    @Builder
    @Schema
    public static class Tree implements Serializable {
        @Serial
        private static final long serialVersionUID = -3916923942990638381L;
        private String id;
        private String label;
        private ServerVO.Server server;
        private Boolean disabled;
        private List<Tree> children;
    }

}