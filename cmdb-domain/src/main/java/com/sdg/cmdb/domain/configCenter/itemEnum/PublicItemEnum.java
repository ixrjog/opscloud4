package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum PublicItemEnum {
    DEPLOY_PATH("DEPLOY_PATH", "持续集成deploy目录"),
    TOMCAT_CONFIG_NAME("TOMCAT_CONFIG_NAME", "tomcat配置文件名称"),
    ADMIN_PASSWD("ADMIN_PASSWD", "初始化系统后的admin密码"),
    IPTABLES_WEBSERVICE_PATH("IPTABLES_WEBSERVICE_PATH", "web-service的iptables配置目录"),
    OFFICE_DMZ_IP_NETWORK_ID("OFFICE_DMZ_IP_NETWORK_ID", "办公网络dmz区ipNetworkId(用于vm主机网络配置)");

    private String itemKey;
    private String itemDesc;

    PublicItemEnum(String itemKey, String itemDesc) {
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
        for (PublicItemEnum itemEnum : PublicItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}