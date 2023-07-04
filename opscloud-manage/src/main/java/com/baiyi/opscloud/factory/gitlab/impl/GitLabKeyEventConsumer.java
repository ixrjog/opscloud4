package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.factory.gitlab.enums.GitLabEventNameEnum;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/29 11:03 上午
 * @Version 1.0
 */
@Component
public class GitLabKeyEventConsumer extends AbstractGitLabEventConsumer {

    private final static GitLabEventNameEnum[] EVENT_NAME_ENUMS = {
            GitLabEventNameEnum.KEY_CREATE,
            GitLabEventNameEnum.KEY_DESTROY};

    protected void process(DatasourceInstance instance, GitLabNotifyParam.SystemHook systemHook) {
        // 用户删除Key
        if (GitLabEventNameEnum.KEY_DESTROY.name().equalsIgnoreCase(systemHook.getEvent_name())) {
            AssetContainer assetContainer = toAsset(instance, systemHook);
            DatasourceInstanceAsset asset = dsInstanceAssetService.getByUniqueKey(assetContainer.getAsset());
            // 删除资产
            if (asset != null) {
                simpleDsAssetFacade.deleteAssetById(asset.getId());
            }
        } else {
            // 用户创建新Key
            super.process(instance, systemHook);
        }
    }

    protected AssetContainer toAsset(DatasourceInstance instance, GitLabNotifyParam.SystemHook systemHook) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(instance.getUuid())
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
    protected GitLabEventNameEnum[] getEventNameEnums() {
        return EVENT_NAME_ENUMS;
    }

    @Override
    protected String getAssetType() {
        return DsAssetTypeConstants.GITLAB_SSHKEY.name();
    }

}
