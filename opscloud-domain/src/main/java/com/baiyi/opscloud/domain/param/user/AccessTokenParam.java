package com.baiyi.opscloud.domain.param.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/12/18 15:52
 * @Version 1.0
 */
public class AccessTokenParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class ApplicationAccessToken {

        private String later;

        @Schema(description = "主键")
        private Integer id;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "是否有效")
        private Boolean valid;

        @Schema(description = "令牌")
        private String token;

        @Schema(description = "令牌标识")
        private String tokenId;

        @Schema(description = "过期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "必须指定过期时间")
        private Date expiredTime;

        @Schema(description = "描述")
        @NotNull(message = "必须指定描述")
        private String comment;

    }

}