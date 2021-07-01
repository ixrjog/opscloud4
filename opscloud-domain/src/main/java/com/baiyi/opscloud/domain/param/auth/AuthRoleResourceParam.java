package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/5/12 2:42 下午
 * @Version 1.0
 */
public class AuthRoleResourceParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    @Builder
    public static class RoleResourcePageQuery extends PageParam {

        @ApiModelProperty(value = "资源组id")
        private Integer groupId;

        @ApiModelProperty(value = "资源id")
        private Integer roleId;

        @ApiModelProperty(value = "是否绑定")
        private Boolean bind;
    }
}
