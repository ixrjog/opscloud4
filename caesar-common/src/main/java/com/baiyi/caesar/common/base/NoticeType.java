package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2020/8/15 2:09 下午
 * @Version 1.0
 */
public enum NoticeType {
    BUILD(0),
    DEPLOYMENT(1);

    private int type;

    NoticeType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
