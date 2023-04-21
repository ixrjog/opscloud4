package com.baiyi.opscloud.datasource.consul.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/14 14:09
 * @Version 1.0
 */
public class ConsulService {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Service implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = 6582576243069037013L;

        private String dc;

        /**
         * 不稳定的节点
         */
        @JsonProperty("ChecksCritical")
        private Integer checksCritical;

        /**
         * 通过检查
         */
        @JsonProperty("ChecksPassing")
        private Integer checksPassing;

        /**
         * 检查警告
         */
        @JsonProperty("ChecksWarning")
        private Integer checksWarning;

        @JsonProperty("ExternalSources")
        private String externalSources;

        /**
         * 服务名称
         */
        @JsonProperty("Name")
        private String name;

        @JsonProperty("Nodes")
        private List<String> nodes;

        @JsonProperty("Tags")
        private List<String> tags;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.name)
                    .name(this.name)
                    .assetKey(Joiner.on(":").join(this.dc, this.name))
                    .assetKey2(this.dc)
                    .isActive(true)
                    .assetType(DsAssetTypeConstants.CONSUL_SERVICE.name())
                    .kind("service")
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("checksCritical", this.checksCritical)
                    .paramProperty("checksPassing", this.checksPassing)
                    .paramProperty("checksWarning", this.checksWarning)
                    .paramProperty("nodes", JSONUtil.writeValueAsString(nodes))
                    .build();
        }
    }

}