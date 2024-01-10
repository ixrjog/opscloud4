package com.baiyi.opscloud.datasource.nacos.entity;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/11/11 4:48 下午
 * @Version 1.0
 */
public class NacosLogin {

    /**
     * {
     *   "accessToken":"XXXXXXXX",
     *   "tokenTtl":18000,
     *   "globalAdmin":true
     * }
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccessToken implements Serializable {

        @Serial
        private static final long serialVersionUID = 35984436328431348L;

        private String accessToken;

        private Integer tokenTtl; //秒

        private Boolean globalAdmin;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

}