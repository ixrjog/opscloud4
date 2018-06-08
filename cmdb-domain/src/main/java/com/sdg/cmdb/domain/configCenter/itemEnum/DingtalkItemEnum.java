package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum DingtalkItemEnum {
    DINGTALK_TOKEN_DEPLOY("DINGTALK_TOKEN_DEPLOY", "token"),
    DINGTALK_TOKEN_FT_BUILD("DINGTALK_TOKEN_FT_BUILD", "前端构建token"),
    DINGTALK_TOKEN_ANDROID_BUILD("DINGTALK_TOKEN_ANDROID_BUILD", "Android构建token"),
    DINGTALK_TOKEN_IOS_BUILD("DINGTALK_TOKEN_IOS_BUILD", "iOS构建token"),
    DINGTALK_WEBHOOK("DINGTALK_WEBHOOK", "WEBHOOK地址");

    private String itemKey;
    private String itemDesc;

    DingtalkItemEnum(String itemKey, String itemDesc) {
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
        for (DingtalkItemEnum itemEnum : DingtalkItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }

}
