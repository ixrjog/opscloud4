package com.baiyi.opscloud.domain.vo.server;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/6 4:27 下午
 * @Version 1.0
 */
public class ServerAttributeVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerAttribute {

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "业务id")
        private Integer businessId;

        @ApiModelProperty(value = "业务类型")
        private Integer businessType;

        @ApiModelProperty(value = "属性类型")
        private String attributeType;

        @ApiModelProperty(value = "属性组名称")
        private String groupName;

        @ApiModelProperty(value = "属性值")
        private String attributes;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }
}
