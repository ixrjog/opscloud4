package com.baiyi.opscloud.common.base;



public enum AccountType {
    LDAP(0),
    ZABBIX(1),
    JUMPSEVER(2),
    RAM(3),
    IAM(4),
    GITLAB(5);

    private int type;

    AccountType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
