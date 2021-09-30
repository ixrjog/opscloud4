package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.aliyuncs.ons.model.v20190214.OnsTopicListResponse;
import com.baiyi.opscloud.datasource.aliyun.convert.enums.OnsMessageTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

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
     * @param entry
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsInstanceInServiceListResponse.InstanceVO entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getInstanceId()) // 资产id = 实例id
                .name(entry.getInstanceName())
                .assetKey(entry.getInstanceId())
                .kind(entry.getInstanceType() == 1 ? "标准版实例" : "铂金版实例")
                .assetType(DsAssetTypeEnum.ONS_ROCKETMQ_INSTANCE.name())
                //.description()
                .expiredTime(entry.getReleaseTime() != null ? new Date(entry.getReleaseTime()) : null) // 铂金版本过期时间
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("independentNaming", entry.getIndependentNaming() ? "拥有独立命名空间(资源命名确保实例内唯一，跨实例之间可重名)" : "无独立命名空间(实例内或者跨实例之间，资源命名必须全局唯一)")
                .paramProperty("instanceStatus", entry.getInstanceStatus())
                .build();
    }

    /**
     * ONS Topic
     *
     * @param dsInstance
     * @param entry
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsTopicListResponse.PublishInfoDo entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getInstanceId())
                .name(entry.getTopic())
                .assetKey(entry.getTopic())
                .assetKey2(entry.getRelationName())
                .kind(OnsMessageTypeEnum.getDesc(entry.getMessageType()))
                .assetType(DsAssetTypeEnum.ONS_ROCKETMQ_TOPIC.name())
                .description(entry.getRemark())
                .createdTime(new Date(entry.getCreateTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    /**
     * ONS Group
     *
     * @param dsInstance
     * @param entry
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, OnsGroupListResponse.SubscribeInfoDo entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getInstanceId())
                .name(entry.getGroupId())
                .assetKey(entry.getGroupId())
                .kind(entry.getGroupType())
                .assetType(DsAssetTypeEnum.ONS_ROCKETMQ_GROUP.name())
                .description(entry.getRemark())
                .createdTime(new Date(entry.getCreateTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
