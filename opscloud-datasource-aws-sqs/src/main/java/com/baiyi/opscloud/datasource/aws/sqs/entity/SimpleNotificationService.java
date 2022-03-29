package com.baiyi.opscloud.datasource.aws.sqs.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/3/29 14:02
 * @Version 1.0
 */
public class SimpleNotificationService {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Topic implements IToAsset, Serializable {

        private static final long serialVersionUID = 7688439944039959106L;

        private String topicArn;

        private String regionId;

        private Map<String, String> attributes;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {

            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.topicArn)
                    .name(StringUtils.substringAfterLast(this.topicArn, ":"))
                    .assetKey(this.topicArn)
                    .assetKey2(this.topicArn)
                    .regionId(this.regionId)
                    .kind("snsTopic")
                    .assetType(DsAssetTypeConstants.SNS_TOPIC.name())
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    // 显示名（可能为空）
                    .paramProperty("DisplayName", this.attributes.get("DisplayName"))
                    // .paramProperty("Owner", this.attributes.get("Owner"))
                    // .paramProperty("SubscriptionsPending", this.attributes.get("SubscriptionsPending"))
                    // .paramProperty("SubscriptionsConfirmed", this.attributes.get("SubscriptionsConfirmed"))
                    // .paramProperty("SubscriptionsDeleted", this.attributes.get("SubscriptionsDeleted"))
                    // .paramProperty("Policy", this.attributes.get("Policy"))
                    // .paramProperty("EffectiveDeliveryPolicy", this.attributes.get("EffectiveDeliveryPolicy"))
                    .build();
        }
    }

}
