package com.baiyi.opscloud.zabbix.http;

import lombok.Builder;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/23 6:50 下午
 * @Since 1.0
 */
public class ZabbixHostCreateParam {

    @Data
    @Builder
    public static class Interface {
        @Builder.Default
        private Integer type = 1;
        @Builder.Default
        private Integer main = 1;
        @Builder.Default
        private Integer useip = 1;
        private String ip;
        @Builder.Default
        private String dns = "";
        @Builder.Default
        private String port = "10050";
    }

    @Data
    @Builder
    public static class Groups {
        private String groupid;
    }

    @Data
    @Builder
    public static class Tags {
        private String tag;
        private String value;
    }

    @Data
    @Builder
    public static class Templates {
        private String templateid;
    }
}
