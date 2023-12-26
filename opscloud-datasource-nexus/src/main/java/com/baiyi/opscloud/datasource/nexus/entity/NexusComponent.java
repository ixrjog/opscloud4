package com.baiyi.opscloud.datasource.nexus.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/4/23 11:24
 * @Version 1.0
 */
public class NexusComponent {

    /**
     * https://help.sonatype.com/repomanager3/integrations/rest-and-integration-api/search-api
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Components implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String continuationToken;

        private List<Component> items;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Component implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String id;
        private String repository;
        private String format;
        private String group;
        private String name;
        private String version;
        private List<NexusAsset.Item> assets;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

}