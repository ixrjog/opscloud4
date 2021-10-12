package com.baiyi.opscloud.common.datasource.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/10/12 3:08 下午
 * @Version 1.0
 */
public class DsTencentExmailConfig {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Tencent {
        private Exmail exmail;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Exmail {
        private String apiUrl;
        private String corpId;
        private String name;
        private String corpSecret;
    }

}
