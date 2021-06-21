package com.baiyi.caesar.gitlab.convert;

import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.builder.AssetContainerBuilder;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

/**
 * @Author baiyi
 * @Date 2021/6/21 5:05 下午
 * @Version 1.0
 */
public class GitlabAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabUser gitlabUser) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(gitlabUser.getId()))
                .name(gitlabUser.getName())
                .assetKey(gitlabUser.getUsername())
                .assetKey2(gitlabUser.getEmail())
                .isActive(gitlabUser.isBlocked() == null || !gitlabUser.isBlocked())
                .createdTime(gitlabUser.getCreatedAt())
                .assetType("USER")
                .kind("gitlabUser")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isAdmin", gitlabUser.isAdmin())
                .paramProperty("projectsLimit", gitlabUser.getProjectsLimit())
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
                .assetType("PROJECT")
                .kind("gitlabProject")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("visibility", entry.getVisibility())
                .paramProperty("nameWithNamespace", entry.getNameWithNamespace())
                .paramProperty("httUrl",entry.getHttpUrl())
                .paramProperty("namespaceId",entry.getNamespace().getId())
                .paramProperty("namespaceName",entry.getNamespace().getName())
                .paramProperty("namespacePath",entry.getNamespace().getPath())
                .paramProperty("namespaceKind",entry.getNamespace().getKind())
                .paramProperty("defaultBranch",entry.getDefaultBranch())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, GitlabGroup entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(String.valueOf(entry.getId()))
//                .name(gitlabUser.getName())
//                .assetKey(gitlabUser.getUsername())
//                .assetKey2(gitlabUser.getEmail())
//                .isActive(gitlabUser.isBlocked() == null || !gitlabUser.isBlocked())
//                .createdTime(gitlabUser.getCreatedAt())
                .assetType("PROJECT")
                .kind("gitlabProject")
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
//                .paramProperty("isAdmin", gitlabUser.isAdmin())
//                .paramProperty("projectsLimit", gitlabUser.getProjectsLimit())
                .build();
    }
}
