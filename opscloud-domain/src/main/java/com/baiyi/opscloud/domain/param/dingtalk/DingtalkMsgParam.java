package com.baiyi.opscloud.domain.param.dingtalk;

import lombok.Builder;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/18 11:05 上午
 * @Since 1.0
 */

@Data
@Builder
public class DingtalkMsgParam {

    private String msgtype;
    private LinkMsg link;
    private MarkdownMsg markdown;


    @Data
    @Builder
    public static class LinkMsg {
        private String messageUrl;
        private String picUrl;
        private String text;
        private String title;
    }

    @Data
    @Builder
    public static class MarkdownMsg {
        private String text;
        private String title;
    }


}
