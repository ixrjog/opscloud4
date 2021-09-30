package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
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
public class RdsMysqlAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeDBInstancesResponse.DBInstance entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getDBInstanceId()) // 资产id = 实例id
                .name(entry.getDBInstanceDescription())
                .assetKey(entry.getDBInstanceId())
                //.assetKey2()
                .assetType(DsAssetTypeEnum.RDS_MYSQL_INSTANCE.name())
                .kind(entry.getDBInstanceClass()) // 类 rds.mysql.s3.large
                .regionId(entry.getRegionId())
                .zone(entry.getZoneId())
                .createdTime(ComputeAssetConvert.toGmtDate(entry.getCreateTime()))
                .expiredTime(ComputeAssetConvert.toGmtDate(entry.getExpireTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("engine", entry.getEngine()) // e.g: MySQL
                .paramProperty("engineVersion", entry.getEngineVersion()) // e.g: 5.7 ; 8.0
                .paramProperty("payType", entry.getPayType())
                .paramProperty("status", entry.getDBInstanceStatus()) // e.g: Running
                .paramProperty("networkType", entry.getInstanceNetworkType())
                .paramProperty("instanceType", entry.getDBInstanceType())
                .paramProperty("connectionMode", entry.getConnectionMode())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeDatabasesResponse.Database entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getDBInstanceId()) // 资产id = 实例id
                .name(entry.getDBName())
                .assetKey(entry.getDBName())
                //.assetKey2()
                .assetType(DsAssetTypeEnum.RDS_MYSQL_DATABASE.name())
                .kind(entry.getEngine())
                //.regionId()
                //.zone(entry.getZoneId())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("engine", entry.getEngine()) // e.g: MySQL
                .paramProperty("characterSetName", entry.getCharacterSetName()) // e.g: utf8mb4
                .paramProperty("status", entry.getDBStatus())  // e.g: Running
                .build();
    }

}
