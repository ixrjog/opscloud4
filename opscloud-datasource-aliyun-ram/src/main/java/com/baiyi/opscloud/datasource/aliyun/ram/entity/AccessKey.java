package com.baiyi.opscloud.datasource.aliyun.ram.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/12/10 2:44 PM
 * @Version 1.0
 */
public class AccessKey {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);
    }

    @Data
    public static class Key implements IToAsset {
        private String accessKeyId;
        private String status;
        private String createDate;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.accessKeyId)
                    .name(this.accessKeyId)
                    .assetKey(this.accessKeyId)
                    .kind("ramAccessKey")
                    .assetType(DsAssetTypeEnum.RAM_ACCESS_KEY.name())
                    .createdTime(toGmtDate(this.createDate))
                    .isActive("Active".equals(this.status))
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}
