package com.baiyi.opscloud.datasource.gitlab.converter;

import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.datasource.gitlab.entity.GitLabSshKey;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.SshKey;
import org.gitlab4j.api.models.User;

/**
 * @Author baiyi
 * @Date 2021/6/21 5:05 下午
 * @Version 1.0
 */
@Slf4j
public class GitLabAssetConverter {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, User entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entity.getId()))
                .name(entity.getName())
                .assetKey(entity.getUsername())
                .assetKey2(entity.getEmail())
                .isActive("active".equals(entity.getState()))
                .createdTime(entity.getCreatedAt())
                .assetType(DsAssetTypeConstants.GITLAB_USER.name())
                .kind("gitlabUser")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isAdmin", entity.getIsAdmin())
                .paramProperty("projectsLimit", entity.getProjectsLimit())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Project entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entity.getId()))
                .name(entity.getName())
                .assetKey(entity.getSshUrlToRepo())
                .assetKey2(entity.getWebUrl())
                .createdTime(entity.getCreatedAt())
                .description(entity.getDescription())
                .assetType(DsAssetTypeConstants.GITLAB_PROJECT.name())
                .kind("gitlabProject")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("visibility", entity.getVisibility())
                .paramProperty("nameWithNamespace", entity.getNameWithNamespace())
                .paramProperty("httUrl", entity.getHttpUrlToRepo())
                .paramProperty("namespaceId", entity.getNamespace().getId())
                .paramProperty("namespaceName", entity.getNamespace().getName())
                .paramProperty("namespacePath", entity.getNamespace().getPath())
                .paramProperty("namespaceKind", entity.getNamespace().getKind())
                .paramProperty("defaultBranch", entity.getDefaultBranch())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Group entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entity.getId()))
                .name(entity.getName())
                .assetKey(entity.getWebUrl())
                .assetKey2(entity.getPath())
                .description(entity.getDescription())
                .assetType(DsAssetTypeConstants.GITLAB_GROUP.name())
                .kind("gitlabProject")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("visibility", entity.getVisibility())
                .paramProperty("parentId", entity.getParentId())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, SshKey entity) {
        String name = "";
        if(entity instanceof GitLabSshKey) {
            name = ((GitLabSshKey) entity).getUsername();
        }
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entity.getId()))
                .name(name)
                .assetKey(SSHUtil.getFingerprint(entity.getKey()))
                .assetKey2(entity.getKey())
                .assetType(DsAssetTypeConstants.GITLAB_SSHKEY.name())
                .kind("gitlabSshKey")
                .description(entity.getTitle())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("userId", entity.getUserId())
                .build();
    }
    
}