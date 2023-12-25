package com.baiyi.opscloud.datasource.aliyun.ram.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/12/10 1:37 PM
 * @Version 1.0
 */
public class RamUser {

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class User implements IToAsset {
        private String userId;
        private String userName;
        private String displayName;
        private String mobilePhone;
        private String email;
        private String comments;
        private String createDate;
        private String updateDate; // ListUsersResponse

        private String attachDate; // ListEntitiesForPolicy

        public boolean needUpdate() {
            return StringUtils.isNotBlank(this.displayName) || StringUtils.isNotBlank(this.email);
        }

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.userId)
                    .name(this.displayName)
                    .assetKey(this.userName)
                    .assetKey2(this.email)
                    .kind("ramUser")
                    .assetType(DsAssetTypeConstants.RAM_USER.name())
                    .description(this.comments)
                    .createdTime(toUtcDate(this.createDate))
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("mobilePhone", this.mobilePhone)
                    .build();
        }
    }

}