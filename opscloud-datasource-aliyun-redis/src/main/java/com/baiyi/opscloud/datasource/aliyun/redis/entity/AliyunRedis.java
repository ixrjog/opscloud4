package com.baiyi.opscloud.datasource.aliyun.redis.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
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
    public static class KVStoreInstance implements IToAsset {
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

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId)
                    .name(this.instanceName)
                    .assetKey(this.instanceId)
                    .assetKey2(this.userName)
                    .kind(this.instanceType)
                    .assetType(DsAssetTypeConstants.REDIS_INSTANCE.name())
                    .zone(this.zoneId)
                    .createdTime(TimeUtil.toDate(this.createTime, TimeZoneEnum.UTC))
                    .expiredTime(TimeUtil.toDate(this.endTime, TimeZoneEnum.UTC))
                    .regionId(this.regionId)
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("connectionDomain", this.connectionDomain)
                    .paramProperty("qps", this.qPS)
                    .paramProperty("privateIp", this.privateIp)
                    .paramProperty("engineVersion", this.engineVersion)
                    .paramProperty("instanceClass", this.instanceClass)
                    .paramProperty("nodeType", this.nodeType)
                    .paramProperty("capacity", this.capacity) // 实例容量， 单位：MB
                    .paramProperty("connections", this.connections) // 最大连接数
                    .paramProperty("bandwidth", this.bandwidth) // 实例带宽，单位：MB/s。
                    .paramProperty("port", this.port)
                    .build();
        }
    }

}