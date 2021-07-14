package com.baiyi.opscloud.gitlab.convert;

import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;

/**
 * @Author baiyi
 * @Date 2021/6/21 5:05 下午
 * @Version 1.0
 */
@Slf4j
public class GitlabAssetConvert {


    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabUser entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entry.getId()))
                .name(entry.getName())
                .assetKey(entry.getUsername())
                .assetKey2(entry.getEmail())
                .isActive(entry.isBlocked() == null || !entry.isBlocked())
                .createdTime(entry.getCreatedAt())
                .assetType(DsAssetTypeEnum.GITLAB_USER.name())
                .kind("gitlabUser")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isAdmin", entry.isAdmin())
                .paramProperty("projectsLimit", entry.getProjectsLimit())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabProject entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entry.getId()))
                .name(entry.getName())
                .assetKey(entry.getSshUrl())
                .assetKey2(entry.getWebUrl())
                .createdTime(entry.getCreatedAt())
                .description(entry.getDescription())
                .assetType(DsAssetTypeEnum.GITLAB_PROJECT.name())
                .kind("gitlabProject")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("visibility", entry.getVisibility())
                .paramProperty("nameWithNamespace", entry.getNameWithNamespace())
                .paramProperty("httUrl", entry.getHttpUrl())
                .paramProperty("namespaceId", entry.getNamespace().getId())
                .paramProperty("namespaceName", entry.getNamespace().getName())
                .paramProperty("namespacePath", entry.getNamespace().getPath())
                .paramProperty("namespaceKind", entry.getNamespace().getKind())
                .paramProperty("defaultBranch", entry.getDefaultBranch())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabGroup entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entry.getId()))
                .name(entry.getName())
                .assetKey(entry.getWebUrl())
                .assetKey2(entry.getPath())
                .description(entry.getDescription())
                .assetType(DsAssetTypeEnum.GITLAB_GROUP.name())
                .kind("gitlabProject")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("visibility", entry.getVisibility())
                .paramProperty("parentId", entry.getParentId())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabSSHKey entry) {
        GitlabUser gitlabUser = entry.getUser();
        log.info(entry.getKey());
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entry.getId()))
                .name(gitlabUser.getUsername())
                .assetKey(SSHUtil.getFingerprint(entry.getKey()))
                .assetKey2(entry.getKey())
                .assetType(DsAssetTypeEnum.GITLAB_SSHKEY.name())
                .kind("gitlabSshKey")
                .description(entry.getTitle())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("userId", gitlabUser.getId())
                .build();
    }
}
