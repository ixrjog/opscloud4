package com.baiyi.opscloud.datasource.metersphere.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
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
 * @Date 2023/5/15 14:13
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeoBuildHook implements IToAsset, Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    private Integer buildId;

    private Integer projectId;

    private String name;

    private LeoHook.BuildHook hook;

    private boolean success;

    private int code;

    private Object body;

    @Override
    public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(this.buildId))
                .name(this.name)
                .assetKey(String.valueOf(this.buildId))
                .assetKey2(this.projectId != null ? String.valueOf(this.projectId) : null)
                .isActive(true)
                .assetType(DsAssetTypeConstants.METER_SPHERE_BUILD_HOOK.name())
                .createdTime(new Date())
                .kind("hook")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("hook", JSONUtil.writeValueAsString(this.hook))
                .paramProperty("success", this.success)
                .paramProperty("code", this.code)
                .paramProperty("body", JSONUtil.writeValueAsString(this.body))
                .build();
    }

}