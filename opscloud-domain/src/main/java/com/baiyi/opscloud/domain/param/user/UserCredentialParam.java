package com.baiyi.opscloud.domain.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/12/15 18:21
 * @Version 1.0
 */
public class UserCredentialParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Credential {

        @Schema(description = "主键")
        private Integer id;

        @Schema(description = "用户ID")
        private Integer userId;

        @Schema(description = "实例UUID")
        private String instanceUuid;

        @Schema(description = "标题")
        private String title;

        @Schema(description = "凭据类型")
        private Integer credentialType;

        @Schema(description = "凭据内容")
        @NotNull(message = "凭据不能为空")
        private String credential;

        @Schema(description = "凭据指纹")
        private String fingerprint;

        @Schema(description = "有效")
        private Boolean valid;

        @Schema(description = "有效期")
        private Date expiredTime;

        private String comment;

    }

}