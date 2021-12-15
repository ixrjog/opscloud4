package com.baiyi.opscloud.datasource.aliyun.rds.entity;

import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/12/14 8:02 PM
 * @Version 1.0
 */
public class Rds {

    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DBInstanceAttribute extends DBInstance {

        private String iPType;
        private String dBInstanceDiskUsed;
        private String guardDBInstanceName;
        private Boolean canTempUpgrade;
        private String tempUpgradeTimeStart;
        private String tempUpgradeTimeEnd;
        private String tempUpgradeRecoveryTime;
        private String tempUpgradeRecoveryClass;
        private Integer tempUpgradeRecoveryCpu;
        private Integer tempUpgradeRecoveryMemory;
        private String tempUpgradeRecoveryMaxIOPS;
        private String tempUpgradeRecoveryMaxConnections;
        private String dBInstanceClassType;
        private String connectionString;
        private String port;
        private Long dBInstanceMemory;
        private Integer dBInstanceStorage;
        private String readDelayTime;
        private Integer dBMaxQuantity;
        private Integer accountMaxQuantity;
        private String creationTime;
        private String maintainTime;
        private String availabilityValue;
        private Integer maxIOPS;
        private Integer maxConnections;
        private String dBInstanceCPU;
        private String incrementSourceDBInstanceId;
        private String securityIPList;
        private String advancedFeatures;
        private String accountType;
        private String supportUpgradeAccountType;
        private String supportCreateSuperAccount;
        private String currentKernelVersion;
        private String latestKernelVersion;
        private String readonlyInstanceSQLDelayedTime;
        private String securityIPMode;
        private String timeZone;
        private String collation;
        private String dispenseMode;
        private String masterZone;
        private Integer proxyType;
        private String consoleVersion;
        private Boolean multipleTempUpgrade;
        private String originConfiguration;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DBInstance {
        private Integer insId;
        private String dBInstanceId;
        private String dBInstanceDescription;
        private String payType;
        private String dBInstanceType;
        private String regionId;
        private String expireTime;
        private String destroyTime;
        private String dBInstanceStatus;
        private String engine;
        private String dBInstanceNetType;
        private String connectionMode;
        private String lockMode;
        private String category;
        private String dBInstanceStorageType;
        private String dBInstanceClass;
        private String instanceNetworkType;
        private String vpcCloudInstanceId;
        private String lockReason;
        private String zoneId;
        private Boolean mutriORsignle;
        private String createTime;
        private String engineVersion;
        private String guardDBInstanceId;
        private String tempDBInstanceId;
        private String masterInstanceId;
        private String vpcId;
        private String vSwitchId;
        private String replicateId;
        private String resourceGroupId;
        private String autoUpgradeMinorVersion;
        private String dedicatedHostGroupId;
        private String dedicatedHostIdForMaster;
        private String dedicatedHostIdForSlave;
        private String dedicatedHostIdForLog;
        private String dedicatedHostNameForMaster;
        private String dedicatedHostNameForSlave;
        private String dedicatedHostNameForLog;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class Database {
        private String dBName;
        private String dBInstanceId;
        private String engine;
        private String dBStatus;
        private String characterSetName;
        private String dBDescription;

        private String regionId;
    }
}
