package com.baiyi.opscloud.datasource.aliyun.dms.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/12/16 3:10 PM
 * @Version 1.0
 */
public class DmsUser {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User implements IToAsset {

        public Long curExecuteCount;
        public Long curResultCount;
        public String dingRobot;
        public String email;
        public String lastLoginTime;
        public Long maxExecuteCount;
        public Long maxResultCount;
        public String mobile;
        public String nickName;
        public String notificationMode;
        public String parentUid;
        public String signatureMethod;
        public String state;
        public String uid;
        public String userId;
        public String webhook;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.userId)
                    .name(this.nickName)
                    .assetKey(this.uid)
                    .assetKey2(this.parentUid)
                    .kind("user")
                    .assetType(DsAssetTypeConstants.DMS_USER.name())
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("mobile", this.mobile)
                    .paramProperty("notificationMode", this.notificationMode)
                    .paramProperty("email", this.email)
                    .build();
        }
    }

}