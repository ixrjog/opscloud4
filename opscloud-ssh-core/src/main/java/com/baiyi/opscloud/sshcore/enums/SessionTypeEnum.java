package com.baiyi.opscloud.sshcore.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/7/14 5:22 下午
 * @Version 1.0
 */
@Getter
public enum SessionTypeEnum {

    /**
     * 会话类型
     */
    WEB_TERMINAL("WEB_TERMINAL"),
    SSH_SERVER("SSH_SERVER"),
    KUBERNETES_TERMINAL("KUBERNETES_TERMINAL");

    private final String type;

    SessionTypeEnum(String type) {
        this.type = type;
    }

}