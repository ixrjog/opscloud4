package com.baiyi.opscloud.common.datasource.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/6/17 3:51 下午
 * @Version 1.0
 */
public class DsAliyunConfig {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Aliyun {

        private Account account;
        private String regionId;
        private Set<String> regionIds; // 可用区

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Account {
        private String uid;
        private String name;
        private String accessKeyId;
        private String secret;
    }
}
