package com.baiyi.caesar.terminal.enums;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:54 上午
 * @Version 1.0
 */
public enum MessageState {

    LOGIN("LOGIN", "会话初始建立"),
    HEARTBEAT("HEARTBEAT","心跳，保持会话"),
    COMMAND("COMMAND", "交互命令"),
    DUPLICATE_SESSION("DUPLICATE_SESSION","复制会话"),
    DUPLICATE_SESSION_IP("DUPLICATE_SESSION_IP","复制会话"),
    RESIZE("RESIZE", "改变形体"),
    CLOSE("CLOSE", "关闭所有Term会话"),
    LOGOUT("LOGOUT", "关闭Term会话"),
    BATCH_COMMAND("BATCH_COMMAND","批量命令");
    private String state;
    private String desc;

    MessageState(String code, String desc) {
        this.state = code;
        this.desc = desc;
    }

    public String getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescByState(String state) {
        for(MessageState typeEnum : MessageState.values()) {
            if (typeEnum.getState().equals(state)) {
                return typeEnum.getDesc();
            }
        }
        return "未知类型";
    }
}
