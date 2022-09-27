package com.baiyi.opscloud.common.config.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * @Author baiyi
 * @Date 2022/9/27 09:58
 * @Version 1.0
 */

public class CacheProperties {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Repo {

        private String name;

        private Duration ttl;

    }


}
