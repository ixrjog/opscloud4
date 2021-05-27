package com.baiyi.caesar.domain.param.auth;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:43 上午
 * @Version 1.0
 */
public class AuthUserRoleParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "角色id")
        private Integer roleId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UpdateUserRole {

        @ApiModelProperty(value = "用户名")
        @Valid
        private String username;

        @ApiModelProperty(value = "角色ids")
        private List<Integer> roleIds;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserRolesQuery {

        @ApiModelProperty(value = "用户名")
        private String username;

    }

}
