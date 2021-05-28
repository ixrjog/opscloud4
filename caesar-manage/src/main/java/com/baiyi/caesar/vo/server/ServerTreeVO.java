package com.baiyi.caesar.vo.server;

import com.baiyi.caesar.domain.generator.caesar.Server;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/7 2:58 下午
 * @Version 1.0
 */
public class ServerTreeVO {

    @Data
    @Builder
    @ApiModel
    public static class ServerTree {
        private Integer size;
        private Integer userId;
        private String uuid;
        private List<Tree> tree;
    }

    @Data
    @Builder
    @ApiModel
    public static class Tree {
        private String id;
        private String label;
        private Server server;
        private Boolean disabled;
        private List<Tree> children;
    }

}
