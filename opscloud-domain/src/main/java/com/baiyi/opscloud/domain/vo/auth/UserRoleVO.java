package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:44 上午
 * @Version 1.0
 */
public class UserRoleVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class UserRole extends BaseVO {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "显示名")
        private String displayName;

        @Schema(description = "角色ID", example = "1")
        private Integer roleId;

        @Schema(description = "角色名")
        private String roleName;

        @Schema(description = "角色描述")
        private String roleComment;

    }

}