package com.baiyi.opscloud.datasource.huaweicloud.ecs.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/7/12 14:26
 * @Version 1.0
 */
public class HuaweicloudEcs {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ecs implements IToAsset {
        private String id; // serverId
        private String name;
        private String status;
        private String privateIp;
        private String publicIp;
        private String kind;
        private String zone;
        private String created;

        private String regionId;

        private String disk;
        private String vcpus;
        private String ram;

        private String osType;

        public static Date toUtcDate(String time) {
            return com.baiyi.opscloud.core.util.TimeUtil.toDate(time, TimeZoneEnum.UTC);
        }

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.id)
                    .name(this.name)
                    .assetKey(this.privateIp)
                    .assetKey2(this.publicIp)
                    .regionId(this.regionId)
                    .kind(this.kind)
                    .assetType(DsAssetTypeConstants.HUAWEICLOUD_ECS.name())
                    .createdTime(toUtcDate(this.created))
                    .isActive("ACTIVE".equalsIgnoreCase(this.status))
                    .zone(this.zone)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("osType", this.osType)
                    .paramProperty("ram", this.ram)
                    .paramProperty("vcpus", this.vcpus)
                    .paramProperty("disk", this.disk)
                    .build();
        }
    }

}