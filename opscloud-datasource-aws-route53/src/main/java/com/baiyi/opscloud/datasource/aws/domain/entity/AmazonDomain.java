package com.baiyi.opscloud.datasource.aws.domain.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/4/18 17:21
 * @Version 1.0
 */
public class AmazonDomain {

    @Data
    public static class Domain implements IToAsset {
        private String domainName;
        private Boolean autoRenew;
        private Boolean transferLock;
        private Date expiry;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid()).assetId(this.domainName)
                    .name(this.domainName)
                    .assetKey(this.domainName)
                    .kind("domain")
                    .assetType(DsAssetTypeConstants.AMAZON_DOMAIN.name())
                    .expiredTime(expiry)
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("autoRenew", this.autoRenew)
                    .paramProperty("transferLock", this.transferLock)
                    .build();
        }

    }

}
