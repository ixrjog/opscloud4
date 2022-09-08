package com.baiyi.opscloud.domain.param.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;


public class LoginParam {

    @Builder
    @Data
    @NoArgsConstructor
    @ApiModel
    @AllArgsConstructor
    public static class Login {

        @NotBlank(message = "用户名不能为空")
        @ApiParam(required = true)
        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiParam(required = true)
        @ApiModelProperty(value = "密码")
        private String password;

        @ApiModelProperty(value = "一次性密码(OTP)")
        private String otp;

        public boolean isEmptyPassword() {
            return StringUtils.isEmpty(password);
        }
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    @AllArgsConstructor
    public static class PlatformLogin extends Login implements IAuthPlatform {

        @NotBlank(message = "平台名称不能为空")
        @ApiParam(required = true)
        @ApiModelProperty(value = "平台名称(用于审计)")
        public String platform;

        @NotBlank(message = "平台令牌不能为空")
        @ApiParam(required = true)
        @ApiModelProperty(value = "平台令牌用于鉴权")
        public String platformToken;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Logout {

        @NotBlank(message = "用户名不能为空")
        @ApiParam(required = true)
        @ApiModelProperty(value = "用户名")
        private String username;

        @NotBlank(message = "令牌不能为空")
        @ApiModelProperty(value = "令牌")
        private String token;

    }
}
