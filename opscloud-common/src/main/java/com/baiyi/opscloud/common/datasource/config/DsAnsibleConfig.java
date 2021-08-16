package com.baiyi.opscloud.common.datasource.config;

import com.google.common.base.Joiner;
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

        public static final String PRIVATE_KEY = "/private_key/id_rsa";

        private String version;
        private String ansible;
        private String playbook;
        private String log;
        private String data;

        public String getPrivateKey() {
            return Joiner.on("/").join(data, PRIVATE_KEY);
        }
    }

}
