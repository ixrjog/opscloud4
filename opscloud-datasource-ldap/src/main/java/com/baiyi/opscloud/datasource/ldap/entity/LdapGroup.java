package com.baiyi.opscloud.datasource.ldap.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.*;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

/**
 * @Author baiyi
 * @Date 2021/12/28 5:41 PM
 * @Version 1.0
 */
public class LdapGroup {

    /**
     * @Author baiyi
     * @Date 2019/12/27 5:49 下午
     * @Version 1.0
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Entry(objectClasses = {"groupOfUniqueNames"})
    public static class Group implements IToAsset {

        /**
         * 主键
         */
        @Attribute
        private String groupId;

        /**
         * 组名
         */
        @Attribute(name = "cn")
        private String groupName;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.groupName) // 资产id = 组名
                    .name(this.groupName)
                    .assetKey(this.groupName)
                    .assetType(DsAssetTypeConstants.GROUP.name())
                    .kind("userGroup")
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}