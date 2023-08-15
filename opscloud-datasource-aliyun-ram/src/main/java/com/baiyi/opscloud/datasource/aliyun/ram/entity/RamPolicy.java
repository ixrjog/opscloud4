package com.baiyi.opscloud.datasource.aliyun.ram.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/12/10 1:54 PM
 * @Version 1.0
 */
public class RamPolicy {

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    // ListPolicies
    @Data
    public static class Policy implements IToAsset {
        private String policyName;
        private String policyType;
        private String description;
        private String defaultVersion;
        private String createDate;
        private String updateDate;
        private Integer attachmentCount;

        private String attachDate; // ListPoliciesForUser

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.policyName)
                    .name(this.policyName)
                    .assetKey(this.policyType)
                    .kind("ramUser")
                    .assetType(DsAssetTypeConstants.RAM_POLICY.name())
                    .description(this.description)
                    .createdTime(toUtcDate(this.createDate))
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}