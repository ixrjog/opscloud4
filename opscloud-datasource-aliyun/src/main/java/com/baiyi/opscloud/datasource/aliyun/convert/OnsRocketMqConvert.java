package com.baiyi.opscloud.datasource.aliyun.convert;

import com.baiyi.opscloud.datasource.aliyun.convert.enums.OnsMessageTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import entity.OnsInstance;
import entity.OnsRocketMqGroup;
import entity.OnsRocketMqTopic;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/9/30 2:42 下午
 * @Version 1.0
 */
public class OnsRocketMqConvert {

    /**
     * ONS Instance
     * https://help.aliyun.com/document_detail/106351.html
     *
     * @param dsInstance
     * @param entity
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsInstance.InstanceBaseInfo entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getInstanceId()) // 资产id = 实例id
                .name(entity.getInstanceName())
                .assetKey(entity.getInstanceId())
                .kind(entity.getInstanceType() == 1 ? "标准版实例" : "铂金版实例")
                .assetType(DsAssetTypeEnum.ONS_ROCKETMQ_INSTANCE.name())
                .regionId(entity.getRegionId())
                .expiredTime(entity.getReleaseTime() != null ? new Date(entity.getReleaseTime()) : null) // 铂金版本过期时间
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("independentNaming", entity.getIndependentNaming() ? "拥有独立命名空间(资源命名确保实例内唯一，跨实例之间可重名)" : "无独立命名空间(实例内或者跨实例之间，资源命名必须全局唯一)")
                .paramProperty("instanceStatus", entity.getInstanceStatus())
                .paramProperty("tcpEndpoint",entity.getEndpoints().getTcpEndpoint())
                .paramProperty("httpInternetEndpoint",entity.getEndpoints().getHttpInternetEndpoint())
                .paramProperty("httpInternetSecureEndpoint",entity.getEndpoints().getHttpInternetSecureEndpoint())
                .paramProperty("httpInternalEndpoint",entity.getEndpoints().getHttpInternalEndpoint())
                .build();
    }

    /**
     * ONS Topic
     *
     * @param dsInstance
     * @param entity
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsRocketMqTopic.Topic entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getInstanceId())
                .name(entity.getTopic())
                .assetKey(entity.getTopic())
                .assetKey2(entity.getRelationName())
                .kind(OnsMessageTypeEnum.getDesc(entity.getMessageType()))
                .assetType(DsAssetTypeEnum.ONS_ROCKETMQ_TOPIC.name())
                .regionId(entity.getRegionId())
                .description(entity.getRemark())
                .createdTime(new Date(entity.getCreateTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    /**
     * ONS Group
     *
     * @param dsInstance
     * @param entity
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsRocketMqGroup.Group entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getInstanceId())
                .name(entity.getGroupId())
                .assetKey(entity.getGroupId())
                .kind(entity.getGroupType())
                .assetType(DsAssetTypeEnum.ONS_ROCKETMQ_GROUP.name())
                .regionId(entity.getRegionId())
                .description(entity.getRemark())
                .createdTime(new Date(entity.getCreateTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
