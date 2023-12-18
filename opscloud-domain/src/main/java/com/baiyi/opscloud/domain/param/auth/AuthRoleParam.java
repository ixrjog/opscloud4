package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


public class AuthRoleParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Role {

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

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AuthRolePageQuery extends SuperPageParam {

        @Schema(description = "角色名称")
        private String roleName;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserTicketOcAuthRoleQuery extends PageParam {

        @Schema(description = "查询名称")
        private String queryName;

        private String username;

        @Schema(description = "工单票据ID")
        private Integer workorderTicketId;

    }

}