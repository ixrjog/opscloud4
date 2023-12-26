package com.baiyi.opscloud.datasource.nacos.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/12 10:04 上午
 * @Version 1.0
 */
public class NacosCluster {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NodesResponse implements Serializable {

        @Serial
        private static final long serialVersionUID = 8010771839187137253L;
        private Integer code;
        private String message;
        private List<Node> data;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Node implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -2017355534427518949L;

        private String address;
        private Integer failAccessCnt;
        private String ip;
        private Integer port;
        private String state;
        private Abilities abilities;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.address)
                    .name(this.address)
                    .assetKey(this.ip)
                    //.assetKey2()
                    .isActive(true)
                    .assetType(DsAssetTypeConstants.NACOS_CLUSTER_NODE.name())
                    .kind("node")
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("state", this.state)
                    .paramProperty("port", this.port)
                    .paramProperty("failAccessCnt", this.failAccessCnt)
                    .paramProperty("supportRemoteMetrics",this.abilities.getConfigAbility().getSupportRemoteMetrics())
                    .paramProperty("supportJraft",this.abilities.getNamingAbility().getSupportJraft())
                    .paramProperty("supportRemoteConnection",this.abilities.getRemoteAbility().getSupportRemoteConnection())
                    .build();
        }
    }

    @Data
    public static class Abilities {
        private ConfigAbility configAbility;
        private NamingAbility namingAbility;
        private RemoteAbility remoteAbility;
    }

    @Data
    public static class ConfigAbility {
        private Boolean supportRemoteMetrics;
    }

    @Data
    public static class NamingAbility {
        private Boolean supportJraft;
    }

    @Data
    public static class RemoteAbility {
        private Boolean supportRemoteConnection;
    }

}