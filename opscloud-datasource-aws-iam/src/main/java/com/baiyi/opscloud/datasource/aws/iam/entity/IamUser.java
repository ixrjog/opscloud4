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
 * @Date 2022/1/21 4:17 PM
 * @Version 1.0
 */
public class IamUser {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class User implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = 4845240806163256599L;

        private String path;
        private String userName;
        private String userId;
        private String arn;
        private Date createDate;
        private Date passwordLastUsed;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.userId)
                    .name(this.userName)
                    .assetKey(this.userName)
                    .assetKey2(this.arn)
                    .kind("iamUser")
                    .assetType(DsAssetTypeConstants.IAM_USER.name())
                    .createdTime(this.createDate)
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}