package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum VcsaItemEnum {
    VCSA_HOST("VCSA_HOST", "VCSA 服务器域名或ip"),
    VCSA_USER("VCSA_USER", "VCSA 管理员账号"),
    VCSA_PASSWD("VCSA_PASSWD", "VCSA 管理员密码");

    private String itemKey;
    private String itemDesc;

    VcsaItemEnum(String itemKey, String itemDesc) {
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
        for (VcsaItemEnum itemEnum : VcsaItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}