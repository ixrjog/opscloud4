package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:43 上午
 * @Version 1.0
 */
public class UserRoleParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserRolePageQuery extends PageParam {

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "角色id")
        private Integer roleId;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserRolesQuery {

        @ApiModelProperty(value = "用户名")
        private String username;

    }
}
