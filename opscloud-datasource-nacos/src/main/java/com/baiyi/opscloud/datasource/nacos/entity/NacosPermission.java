package com.baiyi.opscloud.datasource.nacos.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.datasource.nacos.entity.base.BasePage;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/12 3:33 下午
 * @Version 1.0
 */
public class NacosPermission {

    private static String buildAssetKey(NacosPermission.Permission entity){
        return Joiner.on("#").join(entity.getRole(),entity.getResource(),entity.getAction());
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PermissionsResponse extends BasePage.PageResponse implements Serializable {

        @Serial
        private static final long serialVersionUID = 5679634631994334473L;
        private List<Permission> pageItems;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Permission implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -2841569536441847893L;
        private String action;
        private String resource;
        private String role;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.role)
                    .name(this.role)
                    .assetKey(buildAssetKey(this))
                    .isActive(true)
                    .assetType(DsAssetTypeConstants.NACOS_PERMISSION.name())
                    .kind("permission")
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("action", this.action)
                    .paramProperty("resource", this.resource)
                    .build();
        }

    }

}