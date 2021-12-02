package com.baiyi.opscloud.common.constant;

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
    NOTICE("Notice");

    @Getter
    private final String tag;

    DsInstanceTagConstants(String tag) {
        this.tag = tag;

    }

}
