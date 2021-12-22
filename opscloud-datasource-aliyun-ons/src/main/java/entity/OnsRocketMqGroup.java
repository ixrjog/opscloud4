package entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/12/14 7:06 PM
 * @Version 1.0
 */
public class OnsRocketMqGroup {

    // SubscribeInfoDo
    @Data
    public static class Group implements IToAsset {
        private String owner;
        private String groupId;
        private Long updateTime;
        private String remark;
        private String instanceId;
        private Boolean independentNaming;
        private Long createTime;
        private String groupType;
        private String regionId;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId)
                    .name(this.groupId)
                    .assetKey(this.groupId)
                    .kind(this.groupType)
                    .assetType(DsAssetTypeEnum.ONS_ROCKETMQ_GROUP.name())
                    .regionId(this.regionId)
                    .description(this.remark)
                    .createdTime(new Date(this.createTime))
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }
}
