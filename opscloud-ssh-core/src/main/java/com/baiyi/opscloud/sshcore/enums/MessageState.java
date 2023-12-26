package com.baiyi.opscloud.sshcore.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:54 上午
 * @Version 1.0
 */
@Getter
public enum MessageState {

    /**
     * 消息状态
     */
    LOGIN("LOGIN", "会话初始建立"),
    HEARTBEAT("HEARTBEAT","心跳，保持会话"),
    COMMAND("COMMAND", "交互命令"),
    DUPLICATE_SESSION("DUPLICATE_SESSION","复制会话"),
    DUPLICATE_SESSION_IP("DUPLICATE_SESSION_IP","复制会话"),
    RESIZE("RESIZE", "改变形体"),
    CLOSE("CLOSE", "关闭所有Term会话"),
    LOGOUT("LOGOUT", "关闭Term会话"),
    BATCH_COMMAND("BATCH_COMMAND","批量命令"),

    PLAY("PLAY","播放"),
    ;
    private final String state;
    private final String desc;

    MessageState(String code, String desc) {
        this.state = code;
        this.desc = desc;
    }

    public String getDescByState(String state) {
        return Arrays.stream(MessageState.values()).filter(typeEnum -> typeEnum.getState().equals(state)).findFirst().map(MessageState::getDesc).orElse("undefined");
    }

}