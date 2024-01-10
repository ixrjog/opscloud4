package com.baiyi.opscloud.datasource.nexus.entity;

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
 * @Date 2021/10/15 3:00 下午
 * @Version 1.0
 */
public class NexusAsset {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Assets implements Serializable {

        @Serial
        private static final long serialVersionUID = -8706403916963623866L;

        /**
         * 分页查询 https://help.sonatype.com/repomanager3/rest-and-integration-api/pagination
         */
        private String continuationToken;

        private List<Item> items;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements IToAsset, Serializable {

        @Serial
        private static final long serialVersionUID = 3605486735533585738L;

        private String downloadUrl;
        private String path;
        private String id;
        private String repository;
        private String format;
        private Checksum checksum;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

        @Override
        public AssetContainer toAssetContainer(DatasourceInstance dsInstance) {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(dsInstance.getUuid())
                    .assetId(this.id)
                    .name(this.downloadUrl)
                    .assetKey(this.downloadUrl)
                    .assetKey2(this.repository)
                    .isActive(true)
                    .assetType(DsAssetTypeConstants.NEXUS_ASSET.name())
                    .kind("user")
                    .build();

            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset)
                    .paramProperty("md5", this.checksum.getMd5())
                    .paramProperty("sha1", this.checksum.getSha1())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Checksum {

        private String sha1;
        private String md5;
        private String sha256;
        private String sha512;

    }

}