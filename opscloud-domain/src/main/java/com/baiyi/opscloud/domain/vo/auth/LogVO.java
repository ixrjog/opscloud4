package com.baiyi.opscloud.domain.vo.auth;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author feixue
 */
public class LogVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LoginVO {

        private String name; // 用户显示名
        private String uuid;
        private String token;
    }
}
