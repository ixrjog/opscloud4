package com.baiyi.opscloud.datasource.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author 修远
 * @Date 2022/8/1 6:03 PM
 * @Since 1.0
 */
public class AliyunSmsResponse {

    @Data
    public static class SendBatchSms {

        @JsonProperty("Message")
        private String message;
        @JsonProperty("RequestId")
        private String requestId;
        @JsonProperty("BizId")
        private String bizId;
        @JsonProperty("Code")
        private String code;

    }

}