package com.baiyi.opscloud.domain.param.guacamole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/7/9 1:19 下午
 * @Version 1.0
 */
public class GuacamoleParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {

        private Integer serverId;

        private String protocol;

        private Integer serverAccountId;

        private Integer screenWidth;

        private Integer screenHeight;

        private Integer screenDpi;

    }

}