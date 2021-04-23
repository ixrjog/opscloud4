package com.baiyi.opscloud.zabbix.base;

/**
 * @Author baiyi
 * @Date 2021/2/19 10:07 上午
 * @Version 1.0
 */
public enum InterfaceType {

    AGENT("1"),
    SNMP("2"),
    IPMI("3"),
    JMX("4");

    private String type;

    InterfaceType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
