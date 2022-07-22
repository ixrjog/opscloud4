package com.baiyi.opscloud.datasource.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author 修远
 * @Date 2022/7/23 12:16 AM
 * @Since 1.0
 */
public class AliyunVoiceNotifyResponse {

    @Data
    public static class SingleCallByTts {

        @JsonProperty("Message")
        private String message;

        @JsonProperty("RequestId")
        private String requestId;

        @JsonProperty("CallId")
        private String callId;

        @JsonProperty("Code")
        private String code;

    }


}
