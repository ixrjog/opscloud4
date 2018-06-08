package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum ZabbixItemEnum  {
    ZABBIX_API_URL("ZABBIX_API_URL", "ldap://ldap.demo.com:10389"),
    ZABBIX_API_USER("ZABBIX_API_USER", "uid=admin,ou=system"),
    ZABBIX_API_PASSWD("ZABBIX_API_PASSWD", "管理员密码"),
    ZABBIX_API_KEY("ZABBIX_API_KEY", "ou=users,ou=system");

    private String itemKey;
    private String itemDesc;

    ZabbixItemEnum(String itemKey, String itemDesc) {
        this.itemKey = itemKey;
        this.itemDesc = itemDesc;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemDescByKey(String itemKey) {
        for (ZabbixItemEnum itemEnum : ZabbixItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}