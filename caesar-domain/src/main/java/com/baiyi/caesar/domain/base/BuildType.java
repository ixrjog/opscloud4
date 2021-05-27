package com.baiyi.caesar.domain.base;

/**
 * @Author baiyi
 * @Date 2020/8/28 2:40 下午
 * @Version 1.0
 */
public enum BuildType {

    BUILD(0),
    DEPLOYMENT(1);

    private int type;

    BuildType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
