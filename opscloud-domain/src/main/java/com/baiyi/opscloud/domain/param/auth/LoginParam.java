package com.baiyi.opscloud.domain.param.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import javax.validation.Valid;


public class LoginParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Login {

        @Valid
        @ApiParam(required = true)
        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiParam(required = true)
        @ApiModelProperty(value = "密码")
        private String password;

        public boolean isEmptyPassword() {
            return StringUtils.isEmpty(password);
        }
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Logout {

        @Valid
        @ApiParam(required = true)
        @ApiModelProperty(value = "用户名")
        private String username;

        @Valid
        @ApiModelProperty(value = "令牌")
        private String token;
    }
}
