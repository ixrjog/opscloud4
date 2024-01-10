package com.baiyi.opscloud.datasource.aliyun.ons.entity;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.Data;

/**
 * @Author 修远
 * @Date 2023/9/12 1:16 AM
 * @Since 1.0
 */
public class OnsTopicV5 {

    @Data
    public static class Topic implements IToAsset {
        private String createTime;
        private String instanceId;
        private String messageType;
        private String regionId;
        private String remark;
        private String status;
        private String topicName;
        private String updateTime;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId)
                    .name(this.topicName)
                    .assetKey(this.topicName)
                    .kind(this.messageType)
                    .assetType(DsAssetTypeConstants.ONS5_TOPIC.name())
                    .regionId(this.regionId)
                    .description(this.remark)
                    .createdTime(NewTimeUtil.parse(this.createTime))
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }

   }

}