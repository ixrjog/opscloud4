package com.baiyi.opscloud.domain.vo.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:44 上午
 * @Version 1.0
 */
public class UserRoleVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserRole {

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "显示名")
        private String displayName;

        @ApiModelProperty(value = "角色id",example="1")
        private Integer roleId;

        @ApiModelProperty(value = "角色名")
        private String roleName;

        @ApiModelProperty(value = "角色描述")
        private String roleComment;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }
}
