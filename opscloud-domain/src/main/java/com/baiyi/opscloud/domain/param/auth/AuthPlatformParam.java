package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2022/9/19 10:12
 * @Version 1.0
 */
public class AuthPlatformParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AuthPlatformLogPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "平台ID")
        private Integer platformId;

        @Schema(description = "认证用户名")
        private String username;

        @Schema(description = "是否成功")
        private Boolean result;

        @Schema(description = "展开")
        private Boolean extend;

    }

}