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
public class RoleResourceVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class RoleResource {

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "role主键",example="1")
        private Integer roleId;


        @ApiModelProperty(value = "资源主键",example="1")
        private Integer resourceId;

    }

}
