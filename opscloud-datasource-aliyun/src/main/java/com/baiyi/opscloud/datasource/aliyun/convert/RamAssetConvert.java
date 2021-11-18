package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import static com.baiyi.opscloud.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 7:49 下午
 * @Since 1.0
 */
public class RamAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListUsersResponse.User entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getUserId())
                .name(entity.getDisplayName())
                .assetKey(entity.getUserName())
                .assetKey2(entity.getEmail())
                .kind("ramUser")
                .assetType(DsAssetTypeEnum.RAM_USER.name())
                .description(entity.getComments())
                .createdTime(toGmtDate(entity.getCreateDate()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("mobilePhone", entity.getMobilePhone())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListPoliciesResponse.Policy entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getPolicyName())
                .name(entity.getPolicyName())
                .assetKey(entity.getPolicyType())
                .kind("ramUser")
                .assetType(DsAssetTypeEnum.RAM_POLICY.name())
                .description(entity.getDescription())
                .createdTime(toGmtDate(entity.getCreateDate()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListAccessKeysResponse.AccessKey entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getAccessKeyId())
                .name(entity.getAccessKeyId())
                .assetKey(entity.getAccessKeyId())
                .kind("ramAccessKey")
                .assetType(DsAssetTypeEnum.RAM_ACCESS_KEY.name())
                .createdTime(toGmtDate(entity.getCreateDate()))
                .isActive("Active".equals(entity.getStatus()))
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }


}
