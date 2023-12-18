package com.baiyi.opscloud.domain.vo.user.mfa;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/2/28 10:51 AM
 * @Version 1.0
 */
public class MfaVO {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MFA {

        public static final MFA NOT_SHOW = MFA.builder().show(false).build();

        private String title;

        @Builder.Default
        private Boolean show = true;

        /**
         * 用户名
         */
        private String username;

        /**
         * MFA QR code
         */
        private String qrcode;

        /**
         * 密钥
         */
        private String secret;

    }

}