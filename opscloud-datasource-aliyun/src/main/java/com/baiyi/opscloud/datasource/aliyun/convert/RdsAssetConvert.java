package com.baiyi.opscloud.datasource.aliyun.convert;

import com.baiyi.opscloud.datasource.aliyun.rds.entity.Rds;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/9/29 6:28 下午
 * @Version 1.0
 */
public class RdsAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Rds.DBInstanceAttribute entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getDBInstanceId()) // 资产id = 实例id
                .name(entity.getDBInstanceDescription())
                .assetKey(entity.getDBInstanceId())
                //.assetKey2()
                .assetType(DsAssetTypeEnum.RDS_INSTANCE.name())
                .kind(entity.getDBInstanceClass()) // 类 rds.mysql.s3.large
                .regionId(entity.getRegionId())
                .zone(entity.getZoneId())
                .createdTime(ComputeAssetConvert.toGmtDate(entity.getCreateTime()))
                .expiredTime(ComputeAssetConvert.toGmtDate(entity.getExpireTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("engine", entity.getEngine()) // e.g: MySQL
                .paramProperty("engineVersion", entity.getEngineVersion()) // e.g: 5.7 ; 8.0
                .paramProperty("payType", entity.getPayType())
                .paramProperty("status", entity.getDBInstanceStatus()) // e.g: Running
                .paramProperty("networkType", entity.getInstanceNetworkType())
                .paramProperty("instanceType", entity.getDBInstanceType())
                .paramProperty("connectionMode", entity.getConnectionMode())
                .paramProperty("connectionString",entity.getConnectionString()) // 内网连接地址
                .paramProperty("instanceCPU",entity.getDBInstanceCPU())
                .paramProperty("instanceMemory",entity.getDBInstanceMemory()) // MB
                .paramProperty("instanceStorage",entity.getDBInstanceStorage()) // GB
                .paramProperty("maxIOPS",entity.getMaxIOPS())
                .paramProperty("maxConnections",entity.getMaxConnections())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Rds.Database entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getDBInstanceId()) // 资产id = 实例id
                .name(entity.getDBName())
                .assetKey(entity.getDBName())
                .assetType(DsAssetTypeEnum.RDS_DATABASE.name())
                .kind(entity.getEngine())
                .regionId(entity.getRegionId())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("engine", entity.getEngine()) // e.g: MySQL
                .paramProperty("characterSetName", entity.getCharacterSetName()) // e.g: utf8mb4
                .paramProperty("status", entity.getDBStatus())  // e.g: Running
                .build();
    }

}
