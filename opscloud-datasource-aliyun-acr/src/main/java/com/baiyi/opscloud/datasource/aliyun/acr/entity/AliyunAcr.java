package com.baiyi.opscloud.datasource.aliyun.acr.entity;


import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/8/5 13:36
 * @Version 1.0
 */
public class AliyunAcrInstance {

    public static Date toDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Instance implements IToAsset {

        private String modifiedTime;
        private String instanceName;
        private String createTime;
        private String instanceSpecification;
        private String instanceStatus;
        private String instanceId;
        private String regionId;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId)
                    .name(this.instanceName)
                    .assetKey(this.instanceId)
                    .assetKey2(this.instanceSpecification)
                    .regionId(this.regionId)
                    .kind("acr")
                    .assetType(DsAssetTypeConstants.ACR_INSTANCE.name())
                    .createdTime(toDate(this.createTime))
                    .isActive(true)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("instanceStatus",this.instanceStatus)
                    .build();
        }
    }

}
