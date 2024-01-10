package com.baiyi.opscloud.datasource.dingtalk.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/12/1 4:54 下午
 * @Version 1.0
 */
public class DingtalkMessageParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AsyncSendMessage {
        @JsonProperty("agent_id")
        private Long agentId;

        @JsonProperty("userid_list")
        private String useridList;
        private Msg msg;
    }

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg {
        @Builder.Default
        private String msgtype = "markdown";
        private Link link;
        private Markdown markdown;
    }

    @Data
    @Builder
    public static class Link {
        private String messageUrl;
        private String picUrl;
        private String text;
        private String title;
    }

    /**
     * https://developers.dingtalk.com/document/app/message-types-and-data-format/title-afc-2nh-5kk?spm=ding_open_doc.document.0.0.33776551CYXKgf
     */
    @Data
    @Builder
    public static class Markdown {
        private String text;
        private String title;
    }

}