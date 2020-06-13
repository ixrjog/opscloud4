package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


public class RoleParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "角色名称")
        private String roleName;

        @ApiModelProperty(value = "资源名称")
        private String resourceName;

    }

    @Data
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
