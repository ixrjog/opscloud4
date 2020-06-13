package com.baiyi.opscloud.domain.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:34 上午
 * @Version 1.0
 */
public class UserServerTreeParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserServerTreeQuery {

        private Integer userId;

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "服务器组类型", example = "1")
        private Integer grpType;
    }
}
