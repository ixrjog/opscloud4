package com.baiyi.opscloud.datasource.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2022/9/1 3:39 PM
 * @Since 1.0
 */
public class LXHLMessageResponse {


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendMessage {

        private String code;
        private String requestId;

    }
}
