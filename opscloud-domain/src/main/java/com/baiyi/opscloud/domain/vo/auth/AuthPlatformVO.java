package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/9/19 09:36
 * @Version 1.0
 */
public class AuthPlatformVO {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Platform {

        @Schema(description = "平台ID")
        private Integer platformId;

        @Schema(description = "平台名称")
        private String name;

        @Schema(description = "平台说明")
        private String comment;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AuthPlatformLog extends BaseVO implements ReadableTime.IAgo {

        private Integer id;

        @Schema(description = "平台ID")
        private Integer platformId;

        @Schema(description = "平台名称")
        private String platformName;

        @Schema(description = "认证用户名")
        private String username;

        private Boolean otp;

        @Schema(description = "认证结果")
        private Boolean result;

        @Schema(description = "认证消息")
        private String resultMsg;

        private String ago;

        @Override
        public Date getAgoTime() {
            return getCreateTime();
        }

    }

}