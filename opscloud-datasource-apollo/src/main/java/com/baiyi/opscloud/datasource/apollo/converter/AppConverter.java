package com.baiyi.opscloud.datasource.apollo.converter;

import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.ctrip.framework.apollo.openapi.dto.OpenAppDTO;

/**
 * @Author baiyi
 * @Date 2023/5/30 10:09
 * @Version 1.0
 */
public class AppConverter {

    /**
     * Apollo app转换
     *    {
     *         "name":"first_app",
     *         "appId":"100003171",
     *         "orgId":"development",
     *         "orgName":"研发部",
     *         "ownerName":"apollo",
     *         "ownerEmail":"test@test.com",
     *         "dataChangeCreatedBy":"apollo",
     *         "dataChangeLastModifiedBy":"apollo",
     *         "dataChangeCreatedTime":"2019-05-08T09:13:31.000+0800",
     *         "dataChangeLastModifiedTime":"2019-05-08T09:13:31.000+0800"
     *     }
     * @param dsInstance 实例
     * @param entity 条目
     * @return AssetContainer
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, OpenAppDTO entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getAppId())
                .name(entity.getName())
                .assetKey(entity.getAppId())
                .assetKey2(entity.getOrgId())
                .assetType(DsAssetTypeConstants.APOLLO_APP.name())
                .createdTime(entity.getDataChangeCreatedTime())
                .updateTime(entity.getDataChangeLastModifiedTime())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("orgName", entity.getOrgName())
                .paramProperty("ownerName", entity.getOwnerName())
                .paramProperty("ownerEmail", entity.getOwnerEmail())
                .build();
    }

}
