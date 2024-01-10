package com.baiyi.opscloud.datasource.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/5/31 22:58
 * @Version 1.0
 */

public class DingtalkMsg {

    @Data
    @Builder
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg implements Serializable {
        @Serial
        private static final long serialVersionUID = 4414017401317439183L;

        @Builder.Default
        private String msgtype = "markdown";
        private Markdown markdown;
        private At at;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Markdown {

        @Builder.Default
        private String title = "监控告警";
        private String text;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class At {
        private List<String> atMobiles;
        @Builder.Default
        private Boolean isAtAll = false;
    }

}