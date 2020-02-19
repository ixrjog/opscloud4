package com.baiyi.opscloud.domain.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/19 10:50 上午
 * @Version 1.0
 */
public class OcRoleResourceVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class OcRoleResource {

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "role主键")
        private Integer roleId;


        @ApiModelProperty(value = "资源主键")
        private Integer resourceId;

    }

}
