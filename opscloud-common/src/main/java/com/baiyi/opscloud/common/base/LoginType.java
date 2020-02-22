package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/2/22 11:13 上午
 * @Version 1.0
 */
public enum LoginType {

    KEY(0),
    PASSWD(1);

    private int type;

    LoginType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}

