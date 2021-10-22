package com.baiyi.opscloud.datasource.jenkins.convert;

import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
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
public class ComputerAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, ComputerWithDetails entry) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entry.getDisplayName()) // 资产id
                .name(entry.getDisplayName())
                .assetKey(entry.getDisplayName())
                .kind("computer")
                .assetType(DsAssetTypeEnum.JENKINS_COMPUTER.name())
                .isActive(!entry.getOffline())
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("numExecutos", entry.getNumExecutors()) // 并发
                .paramProperty("totalPhysicalMemory",
                        entry.getMonitorData().get("hudson.node_monitors.SwapSpaceMonitor").get("totalPhysicalMemory"))
                .paramProperty("availablePhysicalMemory",
                        entry.getMonitorData().get("hudson.node_monitors.SwapSpaceMonitor").get("availablePhysicalMemory"))
                .paramProperty("remoteRootDirectory",
                        entry.getMonitorData().get("hudson.node_monitors.DiskSpaceMonitor").get("path"))
                .paramProperty("remoteRootDirectorySize",
                        entry.getMonitorData().get("hudson.node_monitors.DiskSpaceMonitor").get("size"))
                .paramProperty("os",
                        entry.getMonitorData().get("hudson.node_monitors.ArchitectureMonitor"))
                .build();
    }
}
