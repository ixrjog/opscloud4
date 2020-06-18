package com.baiyi.opscloud.domain.param.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;


public class LogParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LoginParam {

        @Valid
        @ApiParam(required = true)
        @ApiModelProperty(value = "用户名")
        private String username;

        @Valid
        @ApiParam(required = true)
        @ApiModelProperty(value = "密码")
        private String password;
    }
}
