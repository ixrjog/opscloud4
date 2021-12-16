package com.baiyi.opscloud.datasource.aliyun.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/12/16 9:40 AM
 * @Version 1.0
 */
public class AliyunRedis {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KVStoreInstance {
        private String replacateId;
        private String instanceId;
        private String instanceName;
        private String searchKey;
        private String connectionDomain;
        private Long port;
        private String userName;
        private String instanceStatus;
        private String regionId;
        private Long capacity;
        private String instanceClass;
        private Long qPS;
        private Long bandwidth;
        private Long connections;
        private String zoneId;
        private String config;
        private String chargeType;
        private String networkType;
        private String vpcId;
        private String vSwitchId;
        private String privateIp;
        private String createTime;
        private String endTime;
        private Boolean hasRenewChangeOrder;
        private Boolean isRds;
        private String instanceType;
        private String architectureType;
        private String nodeType;
        private String packageType;
        private String engineVersion;
        private String destroyTime;
        private String connectionMode;
        private String vpcCloudInstanceId;
        private String resourceGroupId;
        private Integer shardCount;
        private Integer proxyCount;
        private String secondaryZoneId;
        private String globalInstanceId;
    }

}
