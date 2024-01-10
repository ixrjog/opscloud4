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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/15 3:11 下午
 * @Version 1.0
 */
public class NacosRole {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RolesResponse extends BasePage.PageResponse implements Serializable {

        @Serial
        private static final long serialVersionUID = -1855804540035445313L;
        private List<Role> pageItems;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Role implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -83954783773211983L;
        private String username;
        private String role;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.username)
                    .name(this.username.replace("LDAP_",""))
                    .assetKey(this.username)
                    .assetKey2(this.role)
                    .isActive(true)
                    .assetType(DsAssetTypeConstants.NACOS_USER.name())
                    .kind("user")
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}