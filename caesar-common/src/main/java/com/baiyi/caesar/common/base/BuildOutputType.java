package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2020/9/2 1:44 下午
 * @Version 1.0
 */
public enum BuildOutputType {

    HTML(0),
    TEXT(1);

    private int type;

    BuildOutputType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
