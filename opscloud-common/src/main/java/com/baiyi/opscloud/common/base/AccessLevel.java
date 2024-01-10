package com.baiyi.opscloud.common.base;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2020/4/11 1:58 上午
 * @Version 1.0
 */
@Getter
public enum AccessLevel {

    /**
     * 访问级别
     */
    ADMIN(100),
    OPS(50),
    DEV(40),
    BASE(10),
    DEF(0);

    private final int level;

    AccessLevel(int level) {
        this.level = level;
    }

}