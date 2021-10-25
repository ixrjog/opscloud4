package com.baiyi.opscloud.datasource.sonar.convert;

import com.baiyi.opscloud.datasource.sonar.entry.base.BaseSonarElement;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/10/25 10:05 上午
 * @Version 1.0
 */
public class SonarAssetConvert {

    /**
     *     {
     *       "organization": "my-org-1",
     *       "id": "project-uuid-1",
     *       "key": "project-key-1",
     *       "name": "Project Name 1",
     *       "qualifier": "TRK",
     *       "visibility": "public",
     *       "lastAnalysisDate": "2017-03-01T11:39:03+0300",
     *       "revision": "cfb82f55c6ef32e61828c4cb3db2da12795fd767"
     *     }
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, BaseSonarElement.Project entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getId())
                .name(entry.getName())
                .assetKey(entry.getKey())
                .assetKey2(entry.getOrganization())
                .isActive(true)
                .assetType(DsAssetTypeEnum.SONAR_PROJECT.name())
                .kind("project")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("revision", entry.getRevision())
                .paramProperty("visibility",entry.getVisibility())
                .build();
    }
}
