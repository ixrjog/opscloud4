package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/18 3:58 下午
 * @Version 1.0
 */
public class ZabbixHostGroup {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryHostGroupResponse extends ZabbixResponse.Response {
        private List<HostGroup> result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CreateHostGroupResponse extends ZabbixResponse.Response {
        private Groupids result;
    }

    @Data
    public static class Groupids {
        private List<String> groupids;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HostGroup implements Serializable {

        private static final long serialVersionUID = -2761527660016718187L;
        @JsonProperty("groupid")
        private String groupid;

        private String name;

        /**
         * 主机组的来源。
         * 0 - 普通的主机组;
         * 4 - 被发现的主机组。
         */
        private Integer flags;

        /**
         * 无论该组是否由系统内部使用，内部组无法被删除。
         * 0 - (默认) 不是内部；
         * 1 - 内部。
         */
        private Integer internal;
    }

}
