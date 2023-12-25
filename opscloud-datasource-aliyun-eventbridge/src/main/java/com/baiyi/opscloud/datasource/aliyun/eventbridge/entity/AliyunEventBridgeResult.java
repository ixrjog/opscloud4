package com.baiyi.opscloud.datasource.aliyun.eventbridge.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/8/31 10:20
 * @Version 1.0
 */
public class AliyunEventBridgeResult {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result implements Serializable {

        @Serial
        private static final long serialVersionUID = -219435505390634328L;
        @JsonProperty("Response")
        private Response response;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response implements Serializable {

        @Serial
        private static final long serialVersionUID = 3868673096309616098L;
        @JsonProperty("RequestId")
        private String requestId;

        @JsonProperty("EventList")
        private List<String> eventList;

    }

}