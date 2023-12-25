package com.baiyi.opscloud.datasource.aliyun.ons.entity;

import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2023/9/12 1:16 AM
 * @Since 1.0
 */
public class OnsGroupV5 {

    @Data
    public static class Group implements IToAsset {

        private String consumerGroupId;
        private String createTime;
        private String instanceId;
        private String regionId;
        private String remark;
        private String status;
        private String updateTime;
        private String deliveryOrderType;
        private ConsumeRetryPolicy consumeRetryPolicy;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId)
                    .name(this.consumerGroupId)
                    .assetKey(this.consumerGroupId)
                    .kind(this.deliveryOrderType)
                    .assetType(DsAssetTypeConstants.ONS5_GROUP.name())
                    .regionId(this.regionId)
                    .description(this.remark)
                    .createdTime(NewTimeUtil.parse(this.createTime))
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ConsumeRetryPolicy {
        private String deadLetterTargetTopic;
        private Integer maxRetryTimes;
        private String retryPolicy;
    }

}