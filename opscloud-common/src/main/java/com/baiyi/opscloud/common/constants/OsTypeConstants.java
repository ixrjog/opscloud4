package com.baiyi.opscloud.common.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/1/25 4:50 PM
 * @Version 1.0
 */
public enum OsTypeConstants {

    LINUX("Linux"),
    WINDOWS("Windows"),
    MACOS("MacOS");

    @Getter
    private final String desc;

    OsTypeConstants(String desc) {
        this.desc = desc;
    }

}
