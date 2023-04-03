package com.baiyi.opscloud.domain.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author feixue
 */
public class LogVO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @Builder
    public static class Login {

        private String name; // 用户显示名
        private String uuid;
        private String token;

    }

}
