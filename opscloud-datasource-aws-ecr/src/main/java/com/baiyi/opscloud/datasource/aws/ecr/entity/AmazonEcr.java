package com.baiyi.opscloud.datasource.aws.ecr.entity;

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

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/8/16 17:32
 * @Version 1.0
 */
public class AmazonEcr {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Repository implements IToAsset {

        private String repositoryArn;
        private String registryId;
        private String repositoryName;
        private String repositoryUri;
        private Date createdAt;
        private String imageTagMutability;
        private String regionId;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.registryId)
                    .name(this.repositoryName)
                    .assetKey(this.repositoryArn)
                    .assetKey2(this.repositoryUri)
                    .regionId(regionId)
                    .kind("repo")
                    .assetType(DsAssetTypeConstants.ECR_REPOSITORY.name())
                    .createdTime(createdAt)
                    .isActive(true)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("imageTagMutability", this.imageTagMutability)
                    .build();
        }

    }

}
