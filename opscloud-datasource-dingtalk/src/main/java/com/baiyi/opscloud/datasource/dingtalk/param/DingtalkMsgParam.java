package com.baiyi.opscloud.datasource.dingtalk.param;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/11/25 4:24 下午
 * @Version 1.0
 */
public class DingtalkMsgParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class SendMsg {
        private String uid;
        private String accessToken;
        private Set<String> useridList;
        private Msg msg;
    }

    @Data
    @Builder
    public static class Msg {
        private String msgtype;
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

    @Data
    @Builder
    public static class Markdown {
        private String text;
        private String title;
    }
}
