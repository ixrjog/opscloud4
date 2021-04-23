package com.baiyi.opscloud.common.base;

import lombok.Getter;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/18 11:21 上午
 * @Since 1.0
 */

@Getter
public enum DingtalkMsgType {

    TEXT("text","文本消息"),
    IMAGE("image","图片消息"),
    VOICE("voice","语音消息"),
    FILE("file","文件消息"),
    LINK("link","链接消息"),
    OA("oa","OA消息"),
    MARKDOWN("markdown","markdown消息"),
    ACTION_CARD("action_card","卡片消息"),
    ;

    private String type;
    private String decs;

    DingtalkMsgType(String type, String decs) {
        this.type = type;
        this.decs = decs;
    }

}
