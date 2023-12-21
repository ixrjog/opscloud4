package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.google.common.base.Joiner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/22 5:13 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnsibleConfig extends BaseDsConfig {

    private Ansible ansible;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Ansible {

        public static final String PRIVATE_KEY = "private_key/id_rsa";

        public static final String INVENTORY_HOSTS = "inventory/ansible_hosts";

        private String version;
        private String ansible;
        private String playbook;
        private String log;
        private String data;

        public String getPrivateKey() {
            return Joiner.on("/").join(data, PRIVATE_KEY);
        }

        public String getInventoryHost() {
            return Joiner.on("/").join(data, INVENTORY_HOSTS);
        }
    }

}