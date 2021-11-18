package com.baiyi.opscloud.datasource.nacos.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/12 10:04 上午
 * @Version 1.0
 */
public class NacosCluster {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NodesResponse implements Serializable {

        private static final long serialVersionUID = 8010771839187137253L;
        private Integer code;
        private String message;
        private List<Node> data;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Node implements Serializable {

        private static final long serialVersionUID = -2017355534427518949L;

        private String address;
        private Integer failAccessCnt;
        private String ip;
        private Integer port;
        private String state;
        private Abilities abilities;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

    @Data
    public static class Abilities {
        private ConfigAbility configAbility;
        private NamingAbility namingAbility;
        private RemoteAbility remoteAbility;
    }

    @Data
    public static class ConfigAbility {
        private Boolean supportRemoteMetrics;
    }

    @Data
    public static class NamingAbility {
        private Boolean supportJraft;
    }

    @Data
    public static class RemoteAbility {
        private Boolean supportRemoteConnection;
    }

}
