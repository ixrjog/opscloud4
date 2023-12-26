package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/12/2 11:06 AM
 * @Version 1.0
 */
@Getter
public enum TagConstants {

    /**
     *
     */
    SYSTEM_HOOKS("SystemHooks"),
    SERVER("Server"),
    AUTHORIZATION("Authorization"),
    ACCOUNT("Account"),
    NOTICE("Notice"),
    SYSTEM("System"),
    MFA("MFA"),
    SUPER_ADMIN("SA"),
    // 阿里云数据库管理
    DMS("DMS"),
    VMS("VMS"),
    SMS("SMS"),
    LEO("Leo");

    private final String tag;

    TagConstants(String tag) {
        this.tag = tag;
    }

}