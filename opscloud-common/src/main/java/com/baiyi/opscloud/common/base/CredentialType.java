package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/2/27 1:04 下午
 * @Version 1.0
 */
public enum  CredentialType {
    PASSWORD(1),
    SSH_PUB_KEY(2),
    SSH_PRIVATE_KEY(3);

    private int type;

    CredentialType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
