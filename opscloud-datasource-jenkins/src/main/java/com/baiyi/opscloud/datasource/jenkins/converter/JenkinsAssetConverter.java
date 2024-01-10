package com.baiyi.opscloud.datasource.jenkins.converter;

import com.baiyi.opscloud.datasource.jenkins.model.ComputerWithDetails;
import com.baiyi.opscloud.datasource.jenkins.model.Job;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

/**
 * @Author baiyi
 * @Date 2021/7/2 10:18 上午
 * @Version 1.0
 */
public class JenkinsAssetConverter {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ComputerWithDetails entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                // 资产id
                .assetId(entity.getDisplayName())
                .name(entity.getDisplayName())
                .assetKey(entity.getDisplayName())
                .kind("computer")
                .assetType(DsAssetTypeConstants.JENKINS_COMPUTER.name())
                .isActive(!entity.getOffline())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                // 并发
                .paramProperty("numExecutors", entity.getNumExecutors())
                .paramProperty("totalPhysicalMemory",
                        entity.getMonitorData().get("hudson.node_monitors.SwapSpaceMonitor").get("totalPhysicalMemory"))
                .paramProperty("availablePhysicalMemory",
                        entity.getMonitorData().get("hudson.node_monitors.SwapSpaceMonitor").get("availablePhysicalMemory"))
                .paramProperty("remoteRootDirectory",
                        entity.getMonitorData().get("hudson.node_monitors.DiskSpaceMonitor").get("path"))
                .paramProperty("remoteRootDirectorySize",
                        entity.getMonitorData().get("hudson.node_monitors.DiskSpaceMonitor").get("size"))
                .paramProperty("os",
                        entity.getMonitorData().get("hudson.node_monitors.ArchitectureMonitor"))
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Job entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getUrl())
                .name(entity.getName())
                .assetKey(entity.getUrl())
                .kind("template")
                .assetType(DsAssetTypeConstants.JENKINS_TEMPLATE.name())
                .build();
        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("_class", entity.get_class())
                .build();
    }

}