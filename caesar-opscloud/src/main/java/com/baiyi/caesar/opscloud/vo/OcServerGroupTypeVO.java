package com.baiyi.caesar.opscloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:21 上午
 * @Version 1.0
 */
public class OcServerGroupTypeVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupType {

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "组类型名称")
        private String name;

        @ApiModelProperty(value = "颜色值")
        private String color;

        @ApiModelProperty(value = "组类型值",example="1")
        private Integer grpType;

        @ApiModelProperty(value = "描述")
        private String comment;

    }
}
