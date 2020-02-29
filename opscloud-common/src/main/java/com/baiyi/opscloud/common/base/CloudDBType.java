package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/2/29 3:07 下午
 * @Version 1.0
 */
public enum  CloudDBType {

    ALIYUN_RDS_MYSQL(2),
    AWS_RDS(3);

    private int type;

    CloudDBType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
