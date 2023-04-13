package com.baiyi.opscloud.zabbix.v5.param;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/8/20 4:38 下午
 * @Version 1.0
 */
public class ZabbixHostParam {

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
    public static class Group {
        private String groupid;
    }

    @Data
    @Builder
    public static class Template {
        private String templateid;
    }

    @Data
    @Builder
    public static class Tag {
        private String tag;
        private String value;
    }

}