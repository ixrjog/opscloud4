package com.baiyi.opscloud.datasource.apollo.entity;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/6/13 15:17
 * @Version 1.0
 */
public class InterceptRelease {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -6025712687441034294L;

        private String appId;

        private String env;

        private String username;

        private String clusterName;

        private String namespaceName;

        private String branchName;

        private Boolean isGray;

        // NamespaceRelease
        private String releaseTitle;
        private String releaseComment;
        private String releasedBy;
        private boolean isEmergencyPublish;

        private boolean success;

        private int code;

        private String msg;

        private Integer ticketId;

        private String action;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(IdUtil.buildUUID())
                    .name(this.appId)
                    .assetKey(this.env)
                    .assetKey2(this.clusterName)
                    .isActive(true)
                    .assetType(DsAssetTypeConstants.APOLLO_INTERCEPT_RELEASE.name())
                    .createdTime(new Date())
                    .kind(this.namespaceName)
                    .description(this.msg)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("success", this.success)
                    .paramProperty("code", this.code)
                    .paramProperty("isGray", this.isGray)
                    .paramProperty("branchName", this.branchName)
                    .paramProperty("username", this.username)
                    .paramProperty("releaseTitle", this.releaseTitle)
                    .paramProperty("ticketId",this.ticketId)
                    .paramProperty("action",this.action)
                    .build();
        }

    }

}
