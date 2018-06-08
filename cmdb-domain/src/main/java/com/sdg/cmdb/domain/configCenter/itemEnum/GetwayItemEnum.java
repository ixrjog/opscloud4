package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum GetwayItemEnum {
    GETWAY_HOST_CONF_FILE("GETWAY_HOST_CONF_FILE", "服务器列表文件"),
    GETWAY_USER_CONF_PATH("GETWAY_USER_CONF_PATH", "用户配置文件"),
    GETWAY_KEY_PATH("GETWAY_KEY_PATH", "getway private key"),
    GETWAY_KEY_FILE("GETWAY_KEY_FILE", "authorized.keys文件名称");

// GETWAY_KEYSTORE_FILE_PATH("GETWAY_KEYSTORE_FILE_PATH", "临时目录"),
    private String itemKey;
    private String itemDesc;

    GetwayItemEnum(String itemKey, String itemDesc) {
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
        for (GetwayItemEnum itemEnum : GetwayItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}