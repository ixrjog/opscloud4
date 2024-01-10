package com.baiyi.opscloud.datasource.aliyun.converter;

import com.aliyun.sdk.service.devops20210625.models.ListProjectsResponseBody;
import com.aliyun.sdk.service.devops20210625.models.ListSprintsResponseBody;
import com.aliyun.sdk.service.devops20210625.models.ListWorkitemsResponseBody;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/5/12 11:11
 * @Version 1.0
 */
public class DevopsAssetConverter {

    /**
     * 项目
     * @param dsInstance
     * @param entity
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListProjectsResponseBody.Projects entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 项目ID
                .assetId(entity.getIdentifier())
                .name(entity.getName())
                .assetKey(entity.getIdentifier())
                // 资源大类型
                .assetKey2(entity.getCategoryIdentifier())
                .assetType(DsAssetTypeConstants.ALIYUN_DEVOPS_PROJECT.name())
                .kind(entity.getScope())

                .createdTime(new Date(entity.getGmtCreate()))
                .expiredTime(entity.getDeleteTime() == null ? null : new Date(entity.getDeleteTime()))
                .description(entity.getDescription())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("icon", entity.getIcon())
                .paramProperty("creator", entity.getCreator())
                .paramProperty("statusStageIdentifier", entity.getStatusStageIdentifier())
                .build();
    }

    /**
     * 迭代
     * @param dsInstance
     * @param entity
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListSprintsResponseBody.Sprints entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 迭代ID
                .assetId(entity.getIdentifier())
                .name(entity.getName())
                // 项目ID
                .assetKey(entity.getSpaceIdentifier())
                .assetType(DsAssetTypeConstants.ALIYUN_DEVOPS_SPRINT.name())
                .kind(entity.getScope())
                .createdTime(new Date(entity.getGmtCreate()))
                .updateTime(entity.getGmtModified() == null ? null : new Date(entity.getGmtModified()))
                .description(entity.getDescription())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                // 状态，TODO，DOING，DONE，分表代表迭代未开始，进行中和已完成
                .paramProperty("status", entity.getStatus())
                // 修改人
                .paramProperty("modifier", entity.getModifier())
                .paramProperty("startDate", entity.getStartDate() == null ? null : new Date(entity.getStartDate()))
                .paramProperty("endDate", entity.getEndDate() == null ? null : new Date(entity.getEndDate()))
                .build();
    }

    /**
     * 工作空间
     * @param dsInstance
     * @param entity
     * @return
     */
    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListWorkitemsResponseBody.Workitems entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 工作项唯一标识
                .assetId(entity.getIdentifier())
                // 工作项标题
                .name(entity.getSubject())
                // 项目ID
                .assetKey(entity.getSpaceIdentifier())
                // 工作项的类型ID: Req、Task
                .assetKey2(entity.getCategoryIdentifier())
                .assetType(DsAssetTypeConstants.ALIYUN_DEVOPS_WORKITEM.name())
                .createdTime(new Date(entity.getGmtCreate()))
                .updateTime(entity.getGmtModified() == null ? null : new Date(entity.getGmtModified()))
                // 工作项内容
                .description(entity.getDocument())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                // 状态名称
                .paramProperty("status", entity.getStatus())
                // 迭代ID
                .paramProperty("sprintIdentifier",entity.getSprintIdentifier())
                .build();
    }

}