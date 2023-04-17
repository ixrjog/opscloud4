package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.*;

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
    @Schema
    public static class PageQuery extends PageParam {

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "角色ID")
        private Integer roleId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateUserRole {

        @Schema(description = "用户名")
        @Valid
        private String username;

        @Schema(description = "角色IDs")
        private List<Integer> roleIds;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UserRolesQuery {

        @Schema(description = "用户名")
        private String username;

    }

}