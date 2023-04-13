package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.base.IRecover;
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
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/18 4:07 下午
 * @Version 1.0
 */
public class ZabbixTrigger {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryTriggerResponse extends ZabbixResponse.Response {
        private List<Trigger> result;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Trigger implements IToAsset, IRecover, Serializable {
        @Serial
        private static final long serialVersionUID = -7481001921036889808L;
        private String triggerid;
        private String description;
        /**
         * 触发器的严重性级别。
         * 0 - (默认) 未分类；
         * 1 - 信息；
         * 2 - 警告；
         * 3 - 一般严重；
         * 4 - 严重；
         * 5 - 灾难。
         */
        private Integer priority;
        //触发器最后更改其状态的时间
        @JsonProperty("lastchange")
        private Long lastchange;
        /**
         * 触发器是否处于正常或故障状态。
         * 0 - (默认) OK; 正常；
         * 1 - 故障。
         */
        private Integer value;

        private List<ZabbixHost.Host> hosts;

        private Integer templateid;

        private String url;

        @Override
        public boolean isRecover() {
            return value == 0;
        }

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.triggerid)
                    .name(this.description)
                    .assetKey(this.triggerid)
                    .kind(String.valueOf(this.priority))
                    .createdTime(new Date(this.lastchange * 1000))
                    .assetType(DsAssetTypeConstants.ZABBIX_TRIGGER.name())
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}