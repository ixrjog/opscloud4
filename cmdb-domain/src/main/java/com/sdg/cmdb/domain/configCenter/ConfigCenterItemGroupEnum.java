package com.sdg.cmdb.domain.configCenter;

/**
 * Created by liangjian on 2017/5/26.
 */
public enum ConfigCenterItemGroupEnum {

    LDAP("LDAP", "LDAP配置组"),
    ZABBIX("ZABBIX", "ZABBIX配置组"),
    ALIYUN_ECS("ALIYUN_ECS", "阿里云ECS配置组"),
    VCSA("VCSA", "VCSA配置组"),
    EMAIL("EMAIL", "EMAIL配置组"),
    DUBBO("DUBBO", "DUBBO&ZK配置组"),
    GETWAY("GETWAY", "GETWAY配置组"),
    DINGTALK("DINGTALK", "DINGTALK配置组"),
    SHADOWSOCKS("SHADOWSOCKS", "SHADOWSOCKS配置组"),
    EXPLAIN_CDL("EXPLAIN_CDL","ExplainCdl配置组"),
    ANSIBLE("ANSIBLE","ANSILBE配置组"),
    JENKINS("JENKINS","JENKINS配置组"),
    PUBLIC("PUBLIC", "公共配置组");

    private String itemKey;
    private String itemDesc;

    ConfigCenterItemGroupEnum(String itemKey, String itemDesc) {
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
        for (ConfigCenterItemGroupEnum itemGroupEnum : ConfigCenterItemGroupEnum.values()) {
            if (itemGroupEnum.getItemKey().equals(itemKey)) {
                return itemGroupEnum.getItemDesc();
            }
        }
        return null;
    }
}
