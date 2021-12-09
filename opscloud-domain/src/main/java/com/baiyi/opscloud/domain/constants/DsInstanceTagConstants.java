package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/12/2 11:06 AM
 * @Version 1.0
 */
public enum DsInstanceTagConstants {
    SYSTEM_HOOKS("SystemHooks"),
    SERVER("Server"),
    AUTHORIZATION("Authorization"),
    ACCOUNT("Account"),
    NOTICE("Notice"),
    SYSTEM("System");

    @Getter
    private final String tag;

    DsInstanceTagConstants(String tag) {
        this.tag = tag;

    }

}
