package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/2/22 5:46 下午
 * @Version 1.0
 */
public enum BusinessType {
    SERVER(1),
    SERVERGROUP(2),
    USER(3),
    USERGROUP(4),
    CLOUD_DATABASE(5),
    /** 服务器器管理员账户 **/
    SERVER_ADMINISTRATOR_ACCOUNT(6),
    ALIYUN_RAM_ACCOUNT(7),
    /** 应用授权 **/
    APPLICATION(8)
    ;

    private int type;

    BusinessType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
