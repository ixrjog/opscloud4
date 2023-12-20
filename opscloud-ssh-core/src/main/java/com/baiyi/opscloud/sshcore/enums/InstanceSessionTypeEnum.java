package com.baiyi.opscloud.sshcore.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/7/21 2:16 下午
 * @Version 1.0
 */
@Getter
public enum InstanceSessionTypeEnum {

    /**
     * 实例会话类型
     */
    SERVER("SERVER"),
    // container
    CONTAINER_LOG("CONTAINER_LOG"),
    CONTAINER_TERMINAL("CONTAINER_TERMINAL");

    private final String type;

    InstanceSessionTypeEnum(String type) {
        this.type = type;
    }

}