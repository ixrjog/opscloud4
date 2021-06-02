package com.baiyi.caesar.domain.vo.auth;

import com.baiyi.caesar.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:44 上午
 * @Version 1.0
 */
public class UserRoleVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserRole extends BaseVO {

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

    }
}
