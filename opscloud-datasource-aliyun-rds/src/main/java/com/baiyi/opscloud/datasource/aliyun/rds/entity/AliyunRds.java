package com.baiyi.opscloud.datasource.aliyun.rds.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.*;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/12/14 8:02 PM
 * @Version 1.0
 */
public class AliyunRds {

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DBInstanceAttribute extends DBInstance implements IToAsset {

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

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.getDBInstanceId()) // 资产id = 实例id
                    .name(this.getDBInstanceDescription())
                    .assetKey(this.getDBInstanceId())
                    //.assetKey2()
                    .assetType(DsAssetTypeConstants.RDS_INSTANCE.name())
                    .kind(this.getDBInstanceClass()) // 类 rds.mysql.s3.large
                    .regionId(this.getRegionId())
                    .zone(this.getZoneId())
                    .createdTime(toUtcDate(this.getCreateTime()))
                    .expiredTime(toUtcDate(this.getExpireTime()))
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("engine", this.getEngine()) // e.g: MySQL
                    .paramProperty("engineVersion", this.getEngineVersion()) // e.g: 5.7 ; 8.0
                    .paramProperty("payType", this.getPayType())
                    .paramProperty("status", this.getDBInstanceStatus()) // e.g: Running
                    .paramProperty("networkType", this.getInstanceNetworkType())
                    .paramProperty("instanceType", this.getDBInstanceType())
                    .paramProperty("connectionMode", this.getConnectionMode())
                    .paramProperty("connectionString", this.getConnectionString()) // 内网连接地址
                    .paramProperty("instanceCPU", this.getDBInstanceCPU())
                    .paramProperty("instanceMemory", this.getDBInstanceMemory()) // MB
                    .paramProperty("instanceStorage", this.getDBInstanceStorage()) // GB
                    .paramProperty("maxIOPS", this.getMaxIOPS())
                    .paramProperty("maxConnections", this.getMaxConnections())
                    .build();
        }
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
    public static class Database implements IToAsset {
        private String dBName;
        private String dBInstanceId;
        private String engine;
        private String dBStatus;
        private String characterSetName;
        private String dBDescription;

        private String regionId;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.dBInstanceId) // 资产id = 实例id
                    .name(this.dBName)
                    .assetKey(this.dBName)
                    .assetType(DsAssetTypeConstants.RDS_DATABASE.name())
                    .kind(this.engine)
                    .regionId(this.regionId)
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("engine", this.engine) // e.g: MySQL
                    .paramProperty("characterSetName", this.characterSetName) // e.g: utf8mb4
                    .paramProperty("status", this.dBStatus)  // e.g: Running
                    .build();
        }
    }

}