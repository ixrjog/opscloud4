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
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AuthRolePageQuery extends SuperPageParam {

        @Schema(name = "角色名称")
        private String roleName;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserTicketOcAuthRoleQuery extends PageParam {

        @Schema(name = "查询名称")
        private String queryName;

        private String username;

        @Schema(name = "工单票据ID")
        private Integer workorderTicketId;

    }


}
