package com.baiyi.opscloud.datasource.aliyun.convert;

import com.baiyi.opscloud.datasource.aliyun.redis.entity.AliyunRedis;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;


/**
 * @Author baiyi
 * @Date 2021/12/16 10:13 AM
 * @Version 1.0
 */
public class RedisAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, AliyunRedis.KVStoreInstance entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getInstanceId())
                .name(entity.getInstanceName())
                .assetKey(entity.getInstanceId())
                .assetKey2(entity.getUserName())
                .kind(entity.getInstanceType())
                .assetType(DsAssetTypeEnum.REDIS_INSTANCE.name())
                //.description(entity.getComments())
                .zone(entity.getZoneId())
                .createdTime(ComputeAssetConvert.toGmtDate(entity.getCreateTime()))
                .expiredTime(ComputeAssetConvert.toGmtDate(entity.getEndTime()))
                .regionId(entity.getRegionId())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("connectionDomain", entity.getConnectionDomain())
                .paramProperty("qps", entity.getQPS())
                .paramProperty("privateIp", entity.getPrivateIp())
                .paramProperty("engineVersion", entity.getEngineVersion())
                .paramProperty("instanceClass", entity.getInstanceClass())
                .paramProperty("nodeType", entity.getNodeType())
                .paramProperty("capacity", entity.getCapacity()) // 实例容量， 单位：MB
                .paramProperty("connections", entity.getConnections()) // 最大连接数
                .paramProperty("bandwidth", entity.getBandwidth()) // 实例带宽，单位：MB/s。
                .paramProperty("port", entity.getPort())
                .build();
    }

}
