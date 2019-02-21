package com.sdg.cmdb.domain.configCenter;

/**
 * Created by liangjian on 2017/5/26.
 */
public enum ConfigCenterItemGroupEnum {


    ALIYUN_ECS("ALIYUN_ECS", "阿里云ECS配置组"),
    VCSA("VCSA", "VCSA配置组"),
    JENKINS("JENKINS","JENKINS配置组");


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
