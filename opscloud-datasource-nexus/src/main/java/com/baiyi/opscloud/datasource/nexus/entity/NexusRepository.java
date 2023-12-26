package com.baiyi.opscloud.datasource.nexus.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/10/15 1:39 下午
 * @Version 1.0
 */
public class NexusRepository {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Repository implements Serializable {

        @Serial
        private static final long serialVersionUID = -6489853813444696312L;
        // Unique
        private String name;
        private String format;
        private String type;
        private String url;
        private Attributes attributes;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attributes {
        private Proxy proxy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Proxy {
        private String remoteUrl;
    }

}