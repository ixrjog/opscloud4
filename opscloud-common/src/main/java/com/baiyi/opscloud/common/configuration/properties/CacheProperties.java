package com.baiyi.opscloud.common.configuration.properties;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
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

        public static final CacheProperties.Repo DEFAULT = CacheProperties.Repo.builder()
                .name(CachingConfiguration.Repositories.DEFAULT)
                .ttl((Duration.ofDays(30)))
                .build();
        public static final CacheProperties.Repo CACHE_FOR_1H = CacheProperties.Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_1H)
                .ttl((Duration.ofHours(1)))
                .build();
        public static final CacheProperties.Repo CACHE_FOR_2H = CacheProperties.Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_2H)
                .ttl((Duration.ofHours(2)))
                .build();
        public static final CacheProperties.Repo CACHE_FOR_1W = CacheProperties.Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_1W)
                .ttl((Duration.ofDays(7)))
                .build();
        public static final CacheProperties.Repo CACHE_FOR_1D = CacheProperties.Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_1D)
                .ttl((Duration.ofDays(1)))
                .build();
        public static final CacheProperties.Repo CACHE_FOR_10S = CacheProperties.Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_10S)
                .ttl((Duration.ofSeconds(10)))
                .build();
        public static final CacheProperties.Repo CACHE_FOR_10M = CacheProperties.Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_10M)
                .ttl((Duration.ofMinutes(10)))
                .build();

        private String name;

        private Duration ttl;

    }

}
