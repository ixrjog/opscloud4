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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/18 1:53 下午
 * @Version 1.0
 */
public class ZabbixHost {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryHostResponse extends ZabbixResponse.Response {
        private List<Host> result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CreateHostResponse extends ZabbixResponse.Response {
        private Hostids result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class UpdateHostResponse extends CreateHostResponse {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class DeleteHostResponse extends CreateHostResponse {
    }

    @Data
    public static class Hostids {
        private List<String> hostids;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Host implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = -1757845550833408363L;
        private String hostid;

        /**
         * 主机的正式名称
         */
        private String host;

        /**
         * Zabbix agent的可用性。
         * 0 - （默认）未知
         * 1 - 可用；
         * 2 - 不可用。
         */
        private Integer available;

        private String description;

        /**
         * 主机的来源。
         * 0 - 普通主机；
         * 4 - 自动发现的主机。
         */
        private Integer flags;

        /**
         * 主机可见名
         */
        private String name;

        /**
         * 主机的状态。
         * 0 - (默认) 已监控的主机；
         * 1 - 未监控的主机。
         */
        private Integer status;

        @JsonProperty("proxy_hostid")
        private String proxyHostid;

        private List<HostInterface> interfaces;

        private List<HostTag> tags;
        private List<HostTag> inheritedTags;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.hostid)
                    .name(this.name)
                    .assetKey(this.interfaces.getFirst().getIp())
                    //.assetKey2()
                    .kind(String.valueOf(this.flags))
                    .isActive(0 == this.status)
                    .assetType(DsAssetTypeConstants.ZABBIX_HOST.name())
                    .description(this.description)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HostTag implements Serializable {
        @Serial
        private static final long serialVersionUID = -4463100077254316308L;
        private String tag;
        private String value;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HostInterface implements Serializable {
        @Serial
        private static final long serialVersionUID = 7124524815535488636L;
        // @JsonProperty("interfaceid")
        private String interfaceid;

        private String dns;

        // @JsonProperty("hostid")
        private String hostid;

        private String ip;

        /**
         * 该接口是否在主机上用作默认接口。主机上只能有一种类型的接口作为默认设置。
         * 0 - 不是默认；
         * 1 - 默认。
         */
        private Integer main;

        private String port;

        /**
         * 接口类型。
         * 1 - agent；
         * 2 - SNMP；
         * 3 - IPMI；
         * 4 - JMX。
         */
        private Integer type;


        /**
         * 是否应通过IP进行连接。
         * <p>
         * 可能的值：
         * 0 - 使用主机DNS名称连接；
         * 1 - 使用该主机接口的主机IP地址进行连接。
         */
        private Integer useip;
    }

}