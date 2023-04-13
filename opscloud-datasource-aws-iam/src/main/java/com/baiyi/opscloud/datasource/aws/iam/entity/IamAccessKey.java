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
 * @Date 2022/1/24 9:47 AM
 * @Version 1.0
 */
public class IamAccessKey {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AccessKey implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = 4294789140463106594L;
        private String userName;
        private String accessKeyId;
        private String status;
        private Date createDate;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.accessKeyId)
                    .name(this.accessKeyId)
                    .assetKey(this.accessKeyId)
                    .assetKey2(this.userName)
                    .kind("iamAccessKey")
                    .assetType(DsAssetTypeConstants.IAM_ACCESS_KEY.name())
                    .createdTime(this.createDate)
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }
}
