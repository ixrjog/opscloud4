package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


public class AuthRoleParam {


    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class AuthRolePageQuery extends PageParam {

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
