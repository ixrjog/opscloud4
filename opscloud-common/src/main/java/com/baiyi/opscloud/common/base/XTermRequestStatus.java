package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:54 上午
 * @Version 1.0
 */
public enum  XTermRequestStatus {

    INITIAL("INITIAL", "会话初始建立"),
    INITIAL_IP("INITIAL_IP", "会话初始建立(单ip)"),
    HEARTBEAT("HEARTBEAT","心跳，保持会话"),
    COMMAND("COMMAND", "交互命令"),
    DUPLICATE_SESSION("DUPLICATE_SESSION","复制会话"),
    DUPLICATE_SESSION_IP("DUPLICATE_SESSION_IP","复制会话"),
    RESIZE("RESIZE", "改变形体"),
    CLOSE("CLOSE", "关闭所有Term会话"),
    LOGOUT("LOGOUT", "关闭Term会话"),
    BATCH_COMMAND("BATCH_COMMAND","批量命令");
    private String code;
    private String desc;

    XTermRequestStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescByCode(String code) {
        for(XTermRequestStatus typeEnum : XTermRequestStatus.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getDesc();
            }
        }
        return "未知类型";
    }
}
