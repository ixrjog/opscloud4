package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


public class AuthRoleVO {

    public interface IRoles {

        String getUsername();

        void setRoles(List<Role> roles);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Role extends BaseVO {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "角色名称")
        private String roleName;

        @Schema(description = "访问级别", example = "50")
        private Integer accessLevel;

        @Schema(description = "角色描述")
        private String comment;

        @Schema(description = "是否支持工单", example = "true")
        private Boolean allowOrder;

    }

}