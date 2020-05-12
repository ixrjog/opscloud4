package com.baiyi.opscloud.domain.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


public class OcRoleVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Role {

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "角色名称")
        private String roleName;

        @ApiModelProperty(value = "访问级别", example = "50")
        private Integer accessLevel;

        @ApiModelProperty(value = "资源路径")
        private String resourceName;

        @ApiModelProperty(value = "角色描述")
        private String comment;

        @ApiModelProperty(value = "是否支持工单", example = "1")
        private Integer inWorkorder;

//        @ApiModelProperty(value = "创建时间")
//        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//        private Date createTime;
//
//        @ApiModelProperty(value = "更新时间")
//        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//        private Date updateTime;
    }


}
