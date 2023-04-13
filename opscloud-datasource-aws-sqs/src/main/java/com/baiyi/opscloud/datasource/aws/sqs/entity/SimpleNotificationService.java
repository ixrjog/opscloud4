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

import java.io.Serial;
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

        @Serial
        private static final long serialVersionUID = 7688439944039959106L;

        // 工单使用
        private String topic;

        private String topicArn;

        private String envName;

        private String regionId;

        private Map<String, String> attributes;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {

            // 标准 FIFO
            String kind = "true".equals(this.attributes.get("FifoTopic")) ? "fifo" : "normal";

            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.topicArn)
                    .name(StringUtils.substringAfterLast(this.topicArn, ":"))
                    .assetKey(this.topicArn)
                    .assetKey2(this.topicArn)
                    .regionId(this.regionId)
                    .kind(kind)
                    .assetType(DsAssetTypeConstants.SNS_TOPIC.name())
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    // 显示名（可能为空）
                    .paramProperty("DisplayName", this.attributes.get("DisplayName"))
                    .paramProperty("FifoTopic", this.attributes.get("FifoTopic"))
                    // .paramProperty("Owner", this.attributes.get("Owner"))
                    // .paramProperty("SubscriptionsPending", this.attributes.get("SubscriptionsPending"))
                    // .paramProperty("SubscriptionsConfirmed", this.attributes.get("SubscriptionsConfirmed"))
                    // .paramProperty("SubscriptionsDeleted", this.attributes.get("SubscriptionsDeleted"))
                    // .paramProperty("Policy", this.attributes.get("Policy"))
                    // .paramProperty("EffectiveDeliveryPolicy", this.attributes.get("EffectiveDeliveryPolicy"))
                    .build();
        }
    }

    /**
     * {
     * "subscriptionArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_perf_topic:01ec9677-aac8-4f1c-a4ca-90eae6b3defe",
     * "owner":"502076313352",
     * "protocol":"sqs",
     * "endpoint":"arn:aws:sqs:eu-west-1:502076313352:transsnet_close_account_perf_queue",
     * "topicArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_perf_topic"
     * }
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Subscription implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -1065834695056874611L;

        private String subscriptionArn;

        private String envName;

        private String regionId;

        private String protocol;

        private String endpoint;

        private String topicArn;

        // 工单使用
        private String queueName;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {

            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.subscriptionArn)
                    // 订阅的主题
                    .name(StringUtils.substringAfterLast(this.topicArn, ":"))
                    // 终端节点
                    .assetKey(this.endpoint)
                    .assetKey2(this.topicArn)
                    .regionId(this.regionId)
                    .kind(this.protocol)
                    .assetType(DsAssetTypeConstants.SNS_SUBSCRIPTION.name())
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}
