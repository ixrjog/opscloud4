package com.baiyi.opscloud.domain.vo.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/15 5:23 下午
 * @Version 1.0
 */
public class GroupVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Group {

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "资源组名称")
        private String groupCode;

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
