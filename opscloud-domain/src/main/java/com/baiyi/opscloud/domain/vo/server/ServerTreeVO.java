package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.vo.tree.TreeVO;
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
    public static class MyServerTree {
        private Integer size;
        private Integer userId;
        private String uuid;
        private List<TreeVO.Tree> tree;
    }


}
