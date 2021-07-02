package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.builder.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import static com.baiyi.opscloud.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 7:49 下午
 * @Since 1.0
 */
public class RamAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListUsersResponse.User entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getUserId())
                .name(entry.getDisplayName())
                .assetKey(entry.getUserName())
                .assetKey2(entry.getEmail())
                .kind("ramUser")
                .assetType(DsAssetTypeEnum.RAM_USER.name())
                .description(entry.getComments())
                .createdTime(toGmtDate(entry.getCreateDate()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("mobilePhone", entry.getMobilePhone())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ListPoliciesResponse.Policy entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getPolicyName())
                .name(entry.getPolicyName())
                .assetKey(entry.getPolicyType())
                .kind("ramUser")
                .assetType(DsAssetTypeEnum.RAM_POLICY.name())
                .description(entry.getDescription())
                .createdTime(toGmtDate(entry.getCreateDate()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
