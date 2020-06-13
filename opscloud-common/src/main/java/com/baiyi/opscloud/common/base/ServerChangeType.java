package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/26 4:49 下午
 * @Version 1.0
 */
public enum ServerChangeType {

    ONLINE("ONLINE"),
    OFFLINE("OFFLINE");

    private String type;

    ServerChangeType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
