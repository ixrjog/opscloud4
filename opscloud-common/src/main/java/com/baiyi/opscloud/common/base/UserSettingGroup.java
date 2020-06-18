package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/17 1:28 下午
 * @Version 1.0
 */
public enum UserSettingGroup {


    XTERM("XTERM");

    private String name;

    UserSettingGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
