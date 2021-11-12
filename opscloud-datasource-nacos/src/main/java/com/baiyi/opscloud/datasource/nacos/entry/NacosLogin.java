package com.baiyi.opscloud.datasource.nacos.entry;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/11/11 4:48 下午
 * @Version 1.0
 */
public class NacosLogin {

    // {"accessToken":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTYwNTYyOTE2Nn0.2TogGhhr11_vLEjqKko1HJHUJEmsPuCxkur-CfNojDo","tokenTtl":18000,"globalAdmin":true}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccessToken implements Serializable {

        private static final long serialVersionUID = 35984436328431348L;

        private String accessToken;

        private Integer tokenTtl;

        private Boolean globalAdmin;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

}
