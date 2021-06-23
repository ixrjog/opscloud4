package com.baiyi.caesar.common.datasource.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/22 5:10 下午
 * @Version 1.0
 */
public class DsAnsibleConfig {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Ansible {

        private String version;
        private String bin;
        private String privateKey;
        private Playbook playbook;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Playbook {
        private String bin;
    }
}
