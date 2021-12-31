package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class AuthRolePageQuery extends SuperPageParam {

        @ApiModelProperty(value = "角色名称")
        private String roleName;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class UserTicketOcAuthRoleQuery extends PageParam {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        private String username;

        @ApiModelProperty(value = "工单票据id")
        private Integer workorderTicketId;

    }


}
