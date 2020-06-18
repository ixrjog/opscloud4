package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/4/13 9:09 上午
 * @Version 1.0
 */
public enum AnsibleResult {

    SUCCESSFUL(0, "SUCCESSFUL"),
    ERROR(1, "ERROR");

    private int type;
    private String name;

    AnsibleResult(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static String getName(int type) {
        for (AnsibleResult result : AnsibleResult.values())
            if (result.getType() == type)
                return result.getName();
        return "FAILED";
    }
}
