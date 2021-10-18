package com.baiyi.opscloud.nexus.entry;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        private static final long serialVersionUID = -8706403916963623866L;

        /**
         * 分页查询 https://help.sonatype.com/repomanager3/rest-and-integration-api/pagination
         */
        private String continuationToken;

        private List<Item> items;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements Serializable {

        private static final long serialVersionUID = 3605486735533585738L;

        private String downloadUrl;
        private String path;
        private String id;
        private String repository;
        private String format;
        private Checksum checksum;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
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
