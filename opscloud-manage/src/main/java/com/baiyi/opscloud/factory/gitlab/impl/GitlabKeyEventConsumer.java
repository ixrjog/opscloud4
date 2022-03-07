package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitlabNotifyParam;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.factory.gitlab.GitlabEventNameEnum;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/29 11:03 上午
 * @Version 1.0
 */
@Component
public class GitlabKeyEventConsumer extends AbstractGitlabEventConsumer {

    private final static GitlabEventNameEnum[] eventNameEnums = {
            GitlabEventNameEnum.KEY_CREATE,
            GitlabEventNameEnum.KEY_DESTROY};

    /**
     * 重写处理方法
     */
    @Override
    protected void proceed() {
        // 用户删除Key
        if (eventContext.get().getSystemHook().getEvent_name().equals(GitlabEventNameEnum.KEY_DESTROY.name())) {
            AssetContainer assetContainer = toAssetContainer();
            DatasourceInstanceAsset asset = dsInstanceAssetService.getByUniqueKey(assetContainer.getAsset());
            if (asset != null) // 删除资产
                simpleDsAssetFacade.deleteAssetById(asset.getId());
        } else {
            super.proceed(); // 用户创建新Key
        }
    }

    protected AssetContainer toAssetContainer() {
        DatasourceInstance dsInstance = eventContext.get().getInstance();
        GitlabNotifyParam.SystemHook systemHook = eventContext.get().getSystemHook();

        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(systemHook.getId()))
                .name(systemHook.getUsername())
                .assetKey(SSHUtil.getFingerprint(systemHook.getKey()))
                .assetKey2(systemHook.getKey())
                .assetType(DsAssetTypeConstants.GITLAB_SSHKEY.name())
                .kind("gitlabSshKey")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .build();
    }

    @Override
    protected GitlabEventNameEnum[] getEventNameEnums() {
        return eventNameEnums;
    }

    @Override
    protected String getAssetType() {
        return DsAssetTypeConstants.GITLAB_SSHKEY.name();
    }

}
