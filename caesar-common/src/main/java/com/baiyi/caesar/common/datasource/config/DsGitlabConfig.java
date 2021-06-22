package com.baiyi.caesar.common.datasource.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/21 4:46 下午
 * @Version 1.0
 */
public class DsGitlabConfig {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Gitlab {

        private String url;
        private String token;
        private SystemHooks systemHooks;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SystemHooks {
        private String token;  // 回调token
    }
}
