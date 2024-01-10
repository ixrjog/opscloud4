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
 * @Date 2023/9/12 1:13 AM
 * @Since 1.0
 */
public class OnsInstanceV5 {

    @Data
    public static class InstanceInfo implements IToAsset {
        private String commodityCode;
        private String createTime;
        private String expireTime;
        private Long groupCount;
        private String instanceId;
        private String instanceName;
        private String paymentType;
        private String regionId;
        private String releaseTime;
        private String remark;
        private String resourceGroupId;
        private String seriesCode;
        private String serviceCode;
        private String startTime;
        private String status;
        private String subSeriesCode;
        private Long topicCount;
        private String updateTime;
        private String userId;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId) // 资产id = 实例id
                    .name(this.instanceName)
                    .assetKey(this.instanceId)
                    /*
                     * standard：标准版
                     * ultimate：铂金版
                     * professional：专业版
                     */
                    .kind(this.seriesCode)
                    .assetType(DsAssetTypeConstants.ONS5_INSTANCE.name())
                    .regionId(this.regionId)
                    .expiredTime(this.expireTime != null ? NewTimeUtil.parse(this.expireTime) : null)
                    .description(this.remark)
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}