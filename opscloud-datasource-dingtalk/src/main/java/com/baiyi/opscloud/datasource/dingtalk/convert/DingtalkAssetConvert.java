package com.baiyi.opscloud.datasource.dingtalk.convert;

import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkDepartment;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkUser;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/11/30 1:06 下午
 * @Version 1.0
 */
public class DingtalkAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DingtalkUser.User entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getUserid())
                .name(entity.getName())
                .assetKey(entity.getUnionid())
                .assetKey2(entity.getEmail())
                .assetType(DsAssetTypeEnum.DINGTALK_USER.name())
                .description(entity.getTitle())
                .isActive(entity.getActive())
                .kind("user")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("username",entity.getUsername())
                .paramProperty("mobile", entity.getMobile())
                .paramProperty("leader", entity.getLeader())
                .paramProperty("avatar", entity.getAvatar())
                .paramProperty("boss", entity.getBoss())
                .paramProperty("admin", entity.getAdmin())
                .paramProperty("jobNumber", entity.getJobNumber())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DingtalkDepartment.Department entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId( String.valueOf(entity.getDeptId()) )
                .name(entity.getName())
                .assetKey(String.valueOf(entity.getDeptId()))
                .assetType(DsAssetTypeEnum.DINGTALK_DEPARTMENT.name())
                .kind("department")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("parentId", entity.getParentId())
                .build();
    }

}
