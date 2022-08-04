package com.baiyi.opscloud.datasource.aliyun.acr.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/8/2 14:18
 * @Version 1.0
 */
public class AliyunEcrRepositories {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);
    }


    /**
     *
     *         "summary":"Automatically created repository",
     *         "repoBuildType":"MANUAL",
     *         "modifiedTime":1658912164000,
     *         "repoId":"crr-o16bjf0i0gwkb4eb",
     *         "createTime":1658911078000,
     *         "repoNamespaceName":"service",
     *         "tagImmutability":false,
     *         "instanceId":"cri-4v9b8l2gc3en0x34",
     *         "repoType":"PRIVATE",
     *         "repoStatus":"NORMAL",
     *         "repoName":"qa-basic-service"
     *
     */
    @Data
    public static class Repositories implements IToAsset  {
        // 摘要信息
        private String summary;

        /**
         * 仓库构建类型，取值：
         *
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
         *
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
                    .build();
        }
    }

}
