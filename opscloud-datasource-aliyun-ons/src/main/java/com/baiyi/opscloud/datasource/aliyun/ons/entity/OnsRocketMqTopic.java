package com.baiyi.opscloud.datasource.aliyun.ons.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.datasource.aliyun.ons.constants.OnsMessageTypeConstants;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/12/14 7:08 PM
 * @Version 1.0
 */
public class OnsRocketMqTopic {

    /**
     * PublishInfoDo
     */
    @Data
    public static class Topic implements IToAsset {
        private String topic;
        private String owner;
        private Integer relation;
        private String relationName;
        private Long createTime;
        private String remark;
        private Integer messageType;
        private String instanceId;
        private Boolean independentNaming;

        private String regionId;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId)
                    .name(this.topic)
                    .assetKey(this.topic)
                    .assetKey2(this.relationName)
                    .kind(OnsMessageTypeConstants.getDesc(this.messageType))
                    .assetType(DsAssetTypeConstants.ONS_ROCKETMQ_TOPIC.name())
                    .regionId(this.regionId)
                    .description(this.remark)
                    .createdTime(new Date(this.createTime))
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }
    }

}