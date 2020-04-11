package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/4/11 1:58 上午
 * @Version 1.0
 */
public enum AccessLevel {

    ADMIN(100),
    OPS(50),
    DEV(40),
    BASE(10);

    private int level;

    AccessLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
