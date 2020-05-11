package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:54 上午
 * @Version 1.0
 */
public enum  XTermRequestStatus {

    /**
     *   keyCode("keyCode", "指令"),
     *       subServerGroup("serverGroup", "基于服务器组订阅"),
     *     subTaskChain("taskChain", "订阅任务链表");
     */

    INITIAL("INITIAL", "会话初始建立"),
    COMMAND("COMMAND", "交互命令"),
    RESIZE("RESIZE", "改变形体"),
    CLOSE("CLOSE", "关闭所有Term会话"),
    LOGOUT("LOGOUT", "关闭Term会话");
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
