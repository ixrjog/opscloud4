package com.baiyi.opscloud.domain.model.property;

import lombok.Data;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/27 10:38 上午
 * @Since 1.0
 */
public class ServerProperty {

    @Data
    public static class Server {
        private String kind = "Server";
        private Metadata metadata;

        private Zabbix zabbix;
        private Ansible ansible;
    }

    @Data
    public static class ServerGroup {
        private String kind = "ServerGroup";
        private Metadata metadata;

        private Zabbix zabbix;
        private Ansible ansible;
    }

    @Data
    public static class Metadata {

        private Integer sshPort = 22;
        private Integer rdpPort = 3389;
        private Integer vncPort = 5901;
        private String manageIp;

    }

    @Data
    public static class Zabbix {

        private Boolean enable = false;
        private List<String> templates;
        private Boolean templateUniformity = false;
        private String proxyName;

    }

    @Data
    public static class Ansible {
        private Integer subgroup;
    }


}
