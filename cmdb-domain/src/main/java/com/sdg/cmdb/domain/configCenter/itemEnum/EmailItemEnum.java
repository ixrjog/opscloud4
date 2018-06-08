package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum EmailItemEnum {
    EMAIL_HOST("EMAIL_HOST", "邮件服务器地址"),
    EMAIL_PORT("EMAIL_PORT", "邮件服务器端口"),
    EMAIL_USERNAME("EMAIL_USERNAME", "邮件服务器用户名"),
    EMAIL_PWD("EMAIL_PWD", "密码");

    private String itemKey;
    private String itemDesc;

    EmailItemEnum(String itemKey, String itemDesc) {
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
        for (EmailItemEnum itemEnum : EmailItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}