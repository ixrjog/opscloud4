package com.baiyi.opscloud.datasource.consul.entity;

import com.baiyi.opscloud.common.base.IToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/22 00:36
 * @Version 1.0
 */
public class ConsulHealth {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Health extends IToString implements Serializable {

        @Serial
        private static final long serialVersionUID = -8728643808503511223L;

        @JsonProperty("Checks")
        private List<Check> checks;

        @JsonProperty("Node")
        private Node node;

        @JsonProperty("Service")
        private Service service;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Check implements Serializable {

        @Serial
        private static final long serialVersionUID = 5915894874497873338L;

        /**
         * CheckID: "serfHealth"
         * CreateIndex: 51519496
         * Definition: {}
         * ModifyIndex: 51519496
         * Name: "Serf Health Status"
         * Node: "account-172.30.10.0"
         * Notes: ""
         * Output: "Agent alive and reachable"
         * ServiceID: ""
         * ServiceName: ""
         * ServiceTags: []
         * Status: "passing"
         * Type: ""
         */

        @JsonProperty("CheckID")
        private String checkID;

        @JsonProperty("CreateIndex")
        private Integer createIndex;

        @JsonProperty("ModifyIndex")
        private String modifyIndex;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Node")
        private String node;

        @JsonProperty("Notes")
        private String notes;

//        @JsonProperty("Output")
//        private String output;

        @JsonProperty("ServiceID")
        private String serviceID;

        @JsonProperty("ServiceName")
        private String serviceName;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("Type")
        private String type;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Node implements Serializable {

        @Serial
        private static final long serialVersionUID = 4553843314261874365L;

        /**
         * Node: {
         * Address: "172.30.10.0"
         * CreateIndex: 51519496
         * Datacenter: "dc1"
         * ID: "48212fc5-1f07-5528-1e0a-f0eea635e9f2"
         * Meta: {consul-network-segment: ""}
         * consul-network-segment: ""
         * ModifyIndex: 51519502
         * Node: "account-172.30.10.0"
         * TaggedAddresses: {lan: "172.30.10.0", wan: "172.30.10.0"}
         * lan: "172.30.10.0"
         * wan: "172.30.10.0"
         */
        @JsonProperty("Address")
        private String address;

        @JsonProperty("CreateIndex")
        private Integer createIndex;

        @JsonProperty("Datacenter")
        private String datacenter;

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Node")
        private String node;

        @JsonProperty("TaggedAddresses")
        private TaggedAddresses taggedAddresses;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaggedAddresses implements Serializable {

        @Serial
        private static final long serialVersionUID = -5231833540389274188L;

        private String lan;

        private String wan;

    }

    /**
     * Service: {ID: "app-172-30-10-0-8080", Service: "account", Tags: [], Address: "172.30.10.0", Meta: null,…}
     * Address: "172.30.10.0"
     * Connect: {}
     * CreateIndex: 51519540
     * EnableTagOverride: false
     * ID: "app-172-30-10-0-8080"
     * Meta: null
     * ModifyIndex: 51519540
     * Port: 8080
     * Proxy: {MeshGateway: {}, Expose: {}}
     * Expose: {}
     * MeshGateway: {}
     * Service: "account"
     * Tags: []
     * Weights: {Passing: 1, Warning: 1}
     * Passing: 1
     * Warning: 1
     */

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Service implements Serializable {

        @Serial
        private static final long serialVersionUID = -8138453725165674673L;

        @JsonProperty("Address")
        private String address;

    }

}
