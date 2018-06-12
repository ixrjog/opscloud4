package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum ZabbixItemEnum  {
    ZABBIX_API_URL("ZABBIX_API_URL", "API接口地址http://company.com/api_jsonrpc.php"),
    ZABBIX_API_USER("ZABBIX_API_USER", "管理员账号"),
    ZABBIX_API_PASSWD("ZABBIX_API_PASSWD", "管理员密码"),
    ZABBIX_API_KEY("ZABBIX_API_KEY", "废除"),
    ZABBIX_API_VERSION("ZABBIX_API_VERSION", "API版本https://www.zabbix.com/documentation/4.0/manual/api");;

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