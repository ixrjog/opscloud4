package com.baiyi.opscloud.datasource.aliyun.eventbridge.entity;

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
 * @Date 2023/8/31 10:44
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeoDeployEvent implements IToAsset, Serializable {

    @Serial
    private static final long serialVersionUID = -7929388965788840998L;

    private Integer deployId;

    private Integer buildId;

    private Integer projectId;

    private String name;

    private LeoHook.DeployHook hook;

    private boolean success;

    private String eventId;

    private String requestId;

    @Override
    public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(eventId)
                .name(this.name)
                .assetKey(String.valueOf(this.deployId))
                .isActive(true)
                .assetType(DsAssetTypeConstants.EVENT_BRIDGE_DEPLOY_EVENT.name())
                .createdTime(new Date())
                .kind("hook")
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("buildId", this.buildId)
                .paramProperty("hook", JSONUtil.writeValueAsString(this.hook))
                .paramProperty("success", this.success)
                .paramProperty("requestId", requestId)
                .build();
    }

}