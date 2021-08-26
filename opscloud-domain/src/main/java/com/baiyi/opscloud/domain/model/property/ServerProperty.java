package com.baiyi.opscloud.domain.model.property;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/27 10:38 上午
 * @Since 1.0
 */
public class ServerProperty {

    @Data
    @Builder
    public static class Server implements Serializable {
        @Builder.Default
        private String kind = "Server";
        @Builder.Default
        private Metadata metadata = Metadata.builder().build();
        @Builder.Default
        private Zabbix zabbix;
    }

    @Builder
    @Data
    public static class Metadata implements Serializable{
        @Builder.Default
        private Integer sshPort = 22;
        @Builder.Default
        private Integer rdpPort = 3389;
        @Builder.Default
        private Integer vncPort = 5901;
        @Builder.Default
        private String manageIp = "";

    }

    @Builder
    @Data
    public static class Zabbix implements Serializable{
        @Builder.Default
        private Boolean enable = false;
        private List<String> templates;
        @Builder.Default
        private Boolean templateUniformity = false;
        private String proxyName;

    }

    @Data
    @Builder
    public static class Ansible implements Serializable{
        @Builder.Default
        private Integer subgroup = 2;
    }


}
