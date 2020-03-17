package com.baiyi.opscloud.domain.param.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


public class LogParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LoginParam {

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "密码")
        private String password;
    }
}
