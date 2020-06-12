package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/6/9 4:12 下午
 * @Version 1.0
 */
public enum RAMType {

    DEFAULT(0),
    USER(1),
    SYSTEM(2);

    private int type;

    RAMType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
