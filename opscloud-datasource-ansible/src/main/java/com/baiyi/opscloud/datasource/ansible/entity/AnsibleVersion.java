package com.baiyi.opscloud.datasource.ansible.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/8/16 11:42 上午
 * @Version 1.0
 */
public class AnsibleVersion {

    public interface VersionType {
        String ANSIBLE = "ANSIBLE";
        String ANSIBLE_PLAYBOOK = "ANSIBLE_PLAYBOOK";
    }

    @Builder
    @Data
    public static class Version implements IToAsset {

        private String version; // 版本
        private String executableLocation; // 执行位置
        private String details; // 详情
        private String type;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.type.equals(AnsibleVersion.VersionType.ANSIBLE) ? "1" : "2")
                    .name(this.type.toLowerCase())
                    .assetKey(this.type)
                    .assetKey2(this.executableLocation)
                    .description(this.details)
                    .isActive(true)
                    .assetType(DsAssetTypeEnum.ANSIBLE_VERSION.name())
                    .kind("ansibleVersion")
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }
}
