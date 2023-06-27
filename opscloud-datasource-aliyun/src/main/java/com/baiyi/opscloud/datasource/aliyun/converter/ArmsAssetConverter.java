package com.baiyi.opscloud.datasource.aliyun.converter;

import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsResponseBody;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/6/27 11:09
 * @Version 1.0
 */
public class ArmsAssetConverter {

    /**
     * https://help.aliyun.com/document_detail/441779.html?spm=a2c4g.441776.0.0.725479e7KWA2bB
     *
     * @param dsInstance
     * @param entity
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListTraceAppsResponseBody.TraceApps entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 项目ID
                .assetId(String.valueOf(entity.getAppId()))
                .name(entity.getAppName())
                .assetKey(entity.getPid())
                .assetType(DsAssetTypeConstants.ALIYUN_ARMS_TRACE_APP.name())
                .kind(entity.getType())
                .regionId(entity.getRegionId())
                .createdTime(new Date(entity.getCreateTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("show", String.valueOf(entity.getShow()))
                .paramProperty("tags", JSONUtil.writeValueAsString(entity.getTags()))
                .build();

    }

}