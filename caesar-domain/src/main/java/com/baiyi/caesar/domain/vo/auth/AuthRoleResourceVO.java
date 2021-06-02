package com.baiyi.caesar.domain.vo.auth;

import com.baiyi.caesar.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/19 10:50 上午
 * @Version 1.0
 */
public class AuthRoleResourceVO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    @Builder
    public static class RoleResource extends BaseVO {

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "role主键",example="1")
        private Integer roleId;

        @ApiModelProperty(value = "资源主键",example="1")
        private Integer resourceId;

    }

}
