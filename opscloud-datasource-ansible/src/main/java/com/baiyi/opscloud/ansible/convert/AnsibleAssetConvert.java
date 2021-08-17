package com.baiyi.opscloud.ansible.convert;

import com.baiyi.opscloud.ansible.model.AnsibleHosts;
import com.baiyi.opscloud.ansible.model.AnsibleVersion;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.datasource.util.SystemEnvUtil;
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

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, AnsibleVersion.Version entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getType().equals(AnsibleVersion.VersionType.ANSIBLE) ? "1" : "2")
                .name(entry.getType().toLowerCase())
                .assetKey(entry.getType())
                .assetKey2(entry.getExecutableLocation())
                .description(entry.getDetails())
                .isActive(true)
                .assetType(DsAssetTypeEnum.ANSIBLE_VERSION.name())
                .kind("ansibleVersion")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, AnsibleHosts.Hosts entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId("1")
                .name(DsAssetTypeEnum.ANSIBLE_HOSTS.name().toLowerCase())
                .assetKey(DsAssetTypeEnum.ANSIBLE_HOSTS.name())
                .assetKey2(SystemEnvUtil.renderEnvHome(entry.getInventoryHost()))
                .description(entry.toInventory())
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
