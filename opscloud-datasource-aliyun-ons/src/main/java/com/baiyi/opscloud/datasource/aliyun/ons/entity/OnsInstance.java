package com.baiyi.opscloud.datasource.aliyun.ons.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/12/14 4:32 PM
 * @Version 1.0
 */
public class OnsInstance {

    /**
     *  https://help.aliyun.com/document_detail/106351.html
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class InstanceBaseInfo implements IToAsset {
        private String instanceId;
        private Integer instanceStatus;
        private Long releaseTime;
        private Integer instanceType;
        private String instanceName;
        private Boolean independentNaming;
        private String remark;
        private Integer topicCapacity;
        private Long maxTps;

        private Endpoints endpoints;

        private String regionId;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId) // 资产id = 实例id
                    .name(this.instanceName)
                    .assetKey(this.instanceId)
                    .kind(1 == this.instanceType ? "标准版实例" : "铂金版实例")
                    .assetType(DsAssetTypeConstants.ONS_ROCKETMQ_INSTANCE.name())
                    .regionId(this.regionId)
                    .expiredTime(this.releaseTime != null ? new Date(this.releaseTime) : null) // 铂金版本过期时间
                    .description(this.remark)
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("independentNaming", this.independentNaming ? "拥有独立命名空间(资源命名确保实例内唯一，跨实例之间可重名)" : "无独立命名空间(实例内或者跨实例之间，资源命名必须全局唯一)")
                    .paramProperty("instanceStatus", this.instanceStatus)
                    .paramProperty("tcpEndpoint", this.endpoints.getTcpEndpoint())
                    .paramProperty("httpInternetEndpoint", this.endpoints.getHttpInternetEndpoint())
                    .paramProperty("httpInternetSecureEndpoint", this.endpoints.getHttpInternetSecureEndpoint())
                    .paramProperty("httpInternalEndpoint", this.endpoints.getHttpInternalEndpoint())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Endpoints {
        private String tcpEndpoint;
        private String httpInternetEndpoint;
        private String httpInternetSecureEndpoint;
        private String httpInternalEndpoint;
    }

    @Data
    public static class Instance {
        private String instanceId;
        private Integer instanceStatus;
        private Long releaseTime;
        private Integer instanceType;
        private String instanceName;
        private Boolean independentNaming;

        private String regionId;
    }

}