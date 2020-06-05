package com.baiyi.opscloud.domain.vo.env;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:33 下午
 * @Version 1.0
 */
public class EnvVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Env {

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "环境名称")
        private String envName;

        @ApiModelProperty(value = "颜色值")
        private String color;

        @ApiModelProperty(value = "环境值",example="1")
        private Integer envType;

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
