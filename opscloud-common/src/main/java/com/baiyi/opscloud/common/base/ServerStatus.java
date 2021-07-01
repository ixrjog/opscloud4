package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/22 2:54 下午
 * @Version 1.0
 */
public enum  ServerStatus {

    UNCERTAIN(-1,"UNCERTAIN"),
    ONLINE(1, "ONLINE"),
    OFFLINE(0, "OFFLINE");

    private int status;
    private String name;

    ServerStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return this.status;
    }

    public String getName() {
        return this.name;
    }

    public static String getName(int status) {
        for (ServerStatus serverStatus : ServerStatus.values())
            if (serverStatus.getStatus() == status)
                return serverStatus.getName();
        return "Null";
    }
}
