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
import java.util.Date;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/3/28 13:56
 * @Version 1.0
 */
public class SimpleQueueService {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Queue implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -6375013141277179377L;

        // 工单使用
        private String queueName;

        private String queueUrl;

        private String envName;

        private String regionId;

        private Map<String, String> attributes;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {

            // 标准 FIFO
            String kind = "true".equals(this.attributes.get("FifoQueue")) ? "fifo" : "normal";

            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.queueUrl)
                    .name(StringUtils.substringAfterLast(this.queueUrl, "/"))
                    .assetKey(this.queueUrl)
                    .assetKey2(attributes.get("QueueArn"))
                    .regionId(this.regionId)
                    .kind(kind)
                    .assetType(DsAssetTypeConstants.SQS.name())
                    .createdTime(new Date(Long.parseLong(this.attributes.get("CreatedTimestamp")) * 1000))
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("ApproximateNumberOfMessagesDelayed", this.attributes.get("ApproximateNumberOfMessagesDelayed"))
                    .paramProperty("ReceiveMessageWaitTimeSeconds", this.attributes.get("ReceiveMessageWaitTimeSeconds"))
                    .paramProperty("SqsManagedSseEnabled", this.attributes.get("SqsManagedSseEnabled"))
                    .paramProperty("DelaySeconds", this.attributes.get("DelaySeconds"))
                    .paramProperty("MessageRetentionPeriod", this.attributes.get("MessageRetentionPeriod"))
                    .paramProperty("MaximumMessageSize", this.attributes.get("MaximumMessageSize"))
                    .paramProperty("VisibilityTimeout", this.attributes.get("VisibilityTimeout"))
                    .paramProperty("ApproximateNumberOfMessages", this.attributes.get("ApproximateNumberOfMessages"))
                    .paramProperty("ApproximateNumberOfMessagesNotVisible", this.attributes.get("ApproximateNumberOfMessagesNotVisible"))
                    .paramProperty("LastModifiedTimestamp", this.attributes.get("LastModifiedTimestamp"))
                    .build();
        }
    }

}
