package com.baiyi.opscloud.domain.vo.server;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/21 1:22 下午
 * @Version 1.0
 */
public class ServerGroupTypeVO {

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

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }
}
