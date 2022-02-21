package com.baiyi.opscloud.datasource.jenkins.converter;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.offbytwo.jenkins.model.ComputerWithDetails;

/**
 * @Author baiyi
 * @Date 2021/7/2 10:18 上午
 * @Version 1.0
 */
public class ComputerAssetConverter {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ComputerWithDetails entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getDisplayName()) // 资产id
                .name(entity.getDisplayName())
                .assetKey(entity.getDisplayName())
                .kind("computer")
                .assetType(DsAssetTypeConstants.JENKINS_COMPUTER.name())
                .isActive(!entity.getOffline())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("numExecutos", entity.getNumExecutors()) // 并发
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
}
