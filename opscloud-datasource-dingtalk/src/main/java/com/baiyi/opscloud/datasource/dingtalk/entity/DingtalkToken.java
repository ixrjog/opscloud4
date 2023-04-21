package com.baiyi.opscloud.datasource.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/11/29 3:04 下午
 * @Version 1.0
 */
public class DingtalkToken {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenResponse extends DingtalkResponse.BaseMsg implements Serializable {
        @Serial
        private static final long serialVersionUID = -4998201798603077254L;
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private Integer expiresIn; // 过期时间 单位秒
    }

}