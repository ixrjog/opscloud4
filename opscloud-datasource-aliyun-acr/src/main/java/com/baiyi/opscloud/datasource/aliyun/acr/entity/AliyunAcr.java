package com.baiyi.opscloud.datasource.aliyun.acr.entity;


import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/8/5 13:36
 * @Version 1.0
 */
public class AliyunAcr {

    public static Date toDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    @Getter
    public enum InstanceSpecification {
        /**
         * 基础实例、标准版实例、高级版实例
         */
        BASIC("Basic", "基础实例"),
        STANDARD("Standard", "标准版实例"),
        ADVANCED("Advanced", "高级版实例");

        private final String spec;

        private final String desc;

        InstanceSpecification(String spec, String desc) {
            this.spec = spec;
            this.desc = desc;
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Instance implements IToAsset {

        private String modifiedTime;
        private String instanceName;
        private String createTime;
        private String instanceSpecification;
        private String instanceStatus;
        private String instanceId;
        private Endpoint endpoint;
        private String regionId;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            Optional<InstanceSpecification> optionalSpec = Arrays.stream(InstanceSpecification.values()).filter(e -> instanceSpecification.endsWith(e.getSpec())).findFirst();
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.instanceId)
                    .name(this.instanceName)
                    .assetKey(this.instanceId)
                    // 转换实例规格
                    .assetKey2(optionalSpec.isPresent() ? optionalSpec.get().getDesc() : this.instanceSpecification)
                    .regionId(this.regionId)
                    .kind("acr")
                    .assetType(DsAssetTypeConstants.ACR_INSTANCE.name())
                    .createdTime(toDate(this.createTime))
                    .isActive(true)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("instanceStatus", this.instanceStatus)
                    .paramProperty("domain", this.endpoint.domain)
                    .paramProperty("instanceSpecification", this.instanceSpecification)
                    //.paramProperty("domainType",this.endpoint.type)
                    .build();
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Endpoint {
        private String type;
        private String domain;
    }

    /**
     * "summary":"Automatically created repository",
     * "repoBuildType":"MANUAL",
     * "modifiedTime":1658912164000,
     * "repoId":"crr-o16bjf0i0gwkb4eb",
     * "createTime":1658911078000,
     * "repoNamespaceName":"service",
     * "tagImmutability":false,
     * "instanceId":"cri-4v9b8l2gc3en0x34",
     * "repoType":"PRIVATE",
     * "repoStatus":"NORMAL",
     * "repoName":"qa-basic-service"
     */
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Repository implements IToAsset {
        // 摘要信息
        private String summary;

        /**
         * 仓库构建类型，取值：
         * <p>
         * AUTO：自动触发构建
         * MANUAL：手动触发构建
         */
        private String repoBuildType;
        // 最近修改时间
        private Long modifiedTime;
        // 仓库ID
        private String repoId;
        // 创建时间
        private Long createTime;
        // 仓库命名空间
        private String repoNamespaceName;
        // 镜像tag不可变性
        private Boolean tagImmutability;
        // 实例ID
        private String instanceId;
        /**
         * 仓库类型，取值：
         * <p>
         * PUBLIC：公开
         * PRIVATE：私有
         */
        private String repoType;
        // 仓库状态
        private String repoStatus;
        // 仓库名称
        private String repoName;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.repoId)
                    .name(this.repoName)
                    .assetKey(this.repoName)
                    .assetKey2(this.repoNamespaceName)
                    .kind("repo")
                    .assetType(DsAssetTypeConstants.ACR_REPOSITORY.name())
                    .createdTime(new Date(this.createTime))
                    .isActive(true)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("instanceId",this.instanceId)
                    .build();
        }
    }

    /**
     * {
     * "defaultRepoType":"PRIVATE",
     * "namespaceStatus":"NORMAL",
     * "namespaceId":"crn-g0h399e0ayt6ax00",
     * "autoCreateRepo":true,
     * "instanceId":"cri-4v9b8l2gc3en0x34",
     * "namespaceName":"daily"
     * }
     */
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Namespace implements IToAsset {

        private String regionId;
        private String defaultRepoType;
        private String namespaceStatus;
        private String namespaceId;
        private Boolean autoCreateRepo;
        private String instanceId;
        private String namespaceName;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.namespaceId)
                    .name(this.namespaceName)
                    .assetKey(this.namespaceId)
                    .assetKey2(this.instanceId)
                    .kind("namespace")
                    .regionId(regionId)
                    .assetType(DsAssetTypeConstants.ACR_NAMESPACE.name())
                    .isActive(true)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("defaultRepoType", this.defaultRepoType)
                    .paramProperty("namespaceStatus", this.namespaceStatus)
                    .paramProperty("autoCreateRepo", this.autoCreateRepo)
                    .build();
        }

    }

}
