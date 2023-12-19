package com.baiyi.opscloud.common.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/1/25 4:50 PM
 * @Version 1.0
 */
@Getter
public enum OsTypeConstants {

    /**
     * 系统类型
     */
    LINUX("Linux"),
    WINDOWS("Windows"),
    MACOS("MacOS");

    private final String desc;

    OsTypeConstants(String desc) {
        this.desc = desc;
    }

}