package com.baiyi.opscloud.datasource.ansible.convert;

import com.baiyi.opscloud.datasource.ansible.model.AnsibleHosts;
import com.baiyi.opscloud.datasource.ansible.model.AnsibleVersion;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

/**
 * @Author baiyi
 * @Date 2021/8/16 3:06 下午
 * @Version 1.0
 */
public class AnsibleAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, AnsibleVersion.Version entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getType().equals(AnsibleVersion.VersionType.ANSIBLE) ? "1" : "2")
                .name(entity.getType().toLowerCase())
                .assetKey(entity.getType())
                .assetKey2(entity.getExecutableLocation())
                .description(entity.getDetails())
                .isActive(true)
                .assetType(DsAssetTypeEnum.ANSIBLE_VERSION.name())
                .kind("ansibleVersion")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, AnsibleHosts.Hosts entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId("1")
                .name(DsAssetTypeEnum.ANSIBLE_HOSTS.name().toLowerCase())
                .assetKey(DsAssetTypeEnum.ANSIBLE_HOSTS.name())
                .assetKey2(SystemEnvUtil.renderEnvHome(entity.getInventoryHost()))
                .description(entity.toInventory())
                .isActive(true)
                .assetType(DsAssetTypeEnum.ANSIBLE_HOSTS.name())
                .kind("ansibleHosts")
                .build();
        IOUtil.writeFile(asset.getDescription(), asset.getAssetKey2());
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }
}
