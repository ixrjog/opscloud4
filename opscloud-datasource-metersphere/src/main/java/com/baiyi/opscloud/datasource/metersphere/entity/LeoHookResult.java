package com.baiyi.opscloud.datasource.metersphere.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2023/5/15 14:06
 * @Version 1.0
 */
public class LeoHookResult {

    @Data
    @Builder
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        public static final Result ERROR =  Result.builder().build();

        private Object body;

        @Builder.Default
        private boolean success = false;

        private int code;

        private String desc;

    }

}
