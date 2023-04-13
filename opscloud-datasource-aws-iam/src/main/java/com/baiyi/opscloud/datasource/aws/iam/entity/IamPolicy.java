package com.baiyi.opscloud.datasource.aws.iam.entity;

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

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/1/21 4:23 PM
 * @Version 1.0
 */
public class IamPolicy {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Policy implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -5742551542170034934L;
        private String policyName;
        private String policyId;
        private String arn;
        private String path;
        private String defaultVersionId;
        private Integer attachmentCount;
        private Integer permissionsBoundaryUsageCount;
        private Boolean isAttachable;
        private String description;
        private Date createDate;
        private Date updateDate;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.policyId)
                    .name(this.policyName)
                    .assetKey(this.policyName)
                    .assetKey2(this.arn)
                    .kind("iamPolicy")
                    .assetType(DsAssetTypeConstants.IAM_POLICY.name())
                    .createdTime(this.createDate)
                    .description(this.description)
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}
