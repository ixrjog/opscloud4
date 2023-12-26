package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/18 3:58 下午
 * @Version 1.0
 */
public class ZabbixHostGroup {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryHostGroupResponse extends ZabbixResponse.Response {
        private List<HostGroup> result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CreateHostGroupResponse extends ZabbixResponse.Response {
        private Groupids result;
    }

    @Data
    public static class Groupids {
        private List<String> groupids;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HostGroup implements IToAsset, Serializable {
        @Serial
        private static final long serialVersionUID = -2761527660016718187L;
        @JsonProperty("groupid")
        private String groupid;

        private String name;

        /**
         * 主机组的来源。
         * 0 - 普通的主机组;
         * 4 - 被发现的主机组。
         */
        private Integer flags;

        /**
         * 无论该组是否由系统内部使用，内部组无法被删除。
         * 0 - (默认) 不是内部；
         * 1 - 内部。
         */
        private Integer internal;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.groupid)
                    .name(this.name)
                    .assetKey(this.groupid)
                    .kind(String.valueOf(this.flags))
                    .assetType(DsAssetTypeConstants.ZABBIX_HOST_GROUP.name())
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}