package com.baiyi.opscloud.domain.model.property;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2021/7/27 10:38 上午
 * @Since 1.0
 */
public class ServerProperty {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Server implements Serializable {

        @Serial
        private static final long serialVersionUID = -4846682649445122975L;
        @Builder.Default
        private String kind = "Server";
        @Builder.Default
        private Metadata metadata = Metadata.builder().build();
        @Builder.Default
        private Zabbix zabbix = Zabbix.builder().build();
        @Builder.Default
        private Ansible ansible = Ansible.builder().build();

        public boolean zabbixEnabled() {
            return Optional.of(this)
                    .map(ServerProperty.Server::getZabbix)
                    .map(ServerProperty.Zabbix::getEnabled)
                    .orElse(false);
        }
        
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata implements Serializable {
        @Serial
        private static final long serialVersionUID = 8854978086918993503L;
        @Builder.Default
        private Integer sshPort = 22;
        @Builder.Default
        private Integer rdpPort = 3389;
        @Builder.Default
        private Integer vncPort = 5900;
        @Builder.Default
        private String manageIp = "";

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Zabbix implements Serializable {

        @Serial
        private static final long serialVersionUID = 5911353965481533349L;
        @Builder.Default
        private Boolean enabled = false;
        private List<String> templates;
        @Builder.Default
        private Boolean templateUniformity = false;
        private String proxyName;
        private List<String> macros;

        public List<Macro> toMacros() {
            if (CollectionUtils.isEmpty(macros)) {
                return Collections.emptyList();
            }
            try {
                return macros.stream().map(e -> new GsonBuilder().create().fromJson(e, Macro.class)).collect(Collectors.toList());
            } catch (Exception e) {
                return Collections.emptyList();
            }
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Macro implements Serializable {
        @Serial
        private static final long serialVersionUID = -5748446862825981795L;
        private String macro;
        private String value;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ansible implements Serializable {
        @Serial
        private static final long serialVersionUID = -8106749818500817348L;
        @Builder.Default
        private Integer subgroup = 2;
    }

}