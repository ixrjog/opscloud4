package com.baiyi.caesar.domain.vo.auth;

import io.swagger.annotations.ApiModel;
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
    @ApiModel
    @Builder
    public static class Login {

        private String name; // 用户显示名
        private String uuid;
        private String token;
    }
}
