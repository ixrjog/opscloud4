package com.baiyi.opscloud.datasource.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/5/31 22:58
 * @Version 1.0
 */

public class DingtalkMsg {

    @Data
    @Builder
    @AllArgsConstructor
    public static class Msg {
        @Builder.Default
        private String msgtype = "markdown";
        private Markdown markdown;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Markdown {
        @Builder.Default
        private String title = "监控告警";
        private String text;

    }
}
