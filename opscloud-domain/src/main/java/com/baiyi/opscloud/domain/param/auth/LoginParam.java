package com.baiyi.opscloud.domain.param.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.lang3.StringUtils;


public class LoginParam {

    @Builder
    @Data
    @NoArgsConstructor
    @Schema
    @AllArgsConstructor
    public static class Login {

        @NotBlank(message = "用户名不能为空")
        @Schema(description = "用户名")
        private String username;

        @Schema(description = "密码")
        private String password;

        @Schema(description = "一次性密码(OTP)")
        private String otp;

        public boolean isEmptyPassword() {
            return StringUtils.isEmpty(password);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    @AllArgsConstructor
    public static class PlatformLogin extends Login implements IAuthPlatform {

        @NotBlank(message = "平台名称不能为空")
        @Schema(description = "平台名称(用于审计)")
        public String platform;

        @NotBlank(message = "平台令牌不能为空")
        @Schema(description = "平台令牌用于鉴权")
        public String platformToken;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Logout {

        @NotBlank(message = "用户名不能为空")
        @Schema(description = "用户名")
        private String username;

        @NotBlank(message = "令牌不能为空")
        @Schema(description = "令牌")
        private String token;

    }

}