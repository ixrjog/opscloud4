package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2020/8/15 2:13 下午
 * @Version 1.0
 */
public enum NoticePhase {

    START(0),
    END(1);

    private int type;

    NoticePhase(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
