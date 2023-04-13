package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.core.asset.IToAsset;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/18 4:04 下午
 * @Version 1.0
 */
public class ZabbixTemplate {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryTemplateResponse extends ZabbixResponse.Response {
        private List<Template> result;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Template implements IToAsset, Serializable {
        @Serial
        private static final long serialVersionUID = 6189522582952178463L;
        //@JsonProperty("templateid")
        private String templateid;
        private String name;
        // 模板的正式名称。
        private String host;
        private String description;

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.templateid)
                    .name(this.name)
                    .assetKey(this.templateid)
                    .assetKey2(this.host)
                    .assetType(DsAssetTypeConstants.ZABBIX_TEMPLATE.name())
                    .kind("zabbixTemplate")
                    .description(this.description)
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .build();
        }
    }

}