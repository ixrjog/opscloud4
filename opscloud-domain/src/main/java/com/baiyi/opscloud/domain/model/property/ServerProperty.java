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

    @Builder
    @Data
    public static class Server implements Serializable {

        private static final long serialVersionUID = -4846682649445122975L;
        @Builder.Default
        private String kind = "Server";
        @Builder.Default
        private Metadata metadata = Metadata.builder().build();
        @Builder.Default
        private Zabbix zabbix;
        @Builder.Default
        private Ansible ansible;
    }

    @Builder
    @Data
    public static class Metadata implements Serializable {
        private static final long serialVersionUID = 8854978086918993503L;
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
    public static class Zabbix implements Serializable {
        private static final long serialVersionUID = 5911353965481533349L;
        @Builder.Default
        private Boolean enabled = false;
        private List<String> templates;
        @Builder.Default
        private Boolean templateUniformity = false;
        private String proxyName;

    }

    @Builder
    @Data
    public static class Ansible implements Serializable {
        private static final long serialVersionUID = -8106749818500817348L;
        @Builder.Default
        private Integer subgroup = 2;
    }


}
