package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum ExplainCdlItemEnum {
    EXPLAIN_CDL_APP_ID("EXPLAIN_CDL_APP_ID", "EXPLAIN_CDL_APP_ID"),
    EXPLAIN_CDL_APP_KEY("EXPLAIN_CDL_APP_KEY", "EXPLAIN_CDL_APP_KEY"),
    EXPLAIN_CDL_APP_NAME("EXPLAIN_CDL_APP_NAME", "EXPLAIN_CDL_APP_NAME"),
    EXPLAIN_CDL_ENV("EXPLAIN_CDL_ENV", "EXPLAIN_CDL_ENV"),
    EXPLAIN_CDL_GROUP_NAME("EXPLAIN_CDL_GROUP_NAME", "EXPLAIN_CDL_GROUP_NAME"),
    EXPLAIN_CDL_LOCAL_PATH("EXPLAIN_CDL_LOCAL_PATH", "EXPLAIN_CDL_LOCAL_PATH"),
    EXPLAIN_GIT_USERNAME("EXPLAIN_GIT_USERNAME", "EXPLAIN_GIT_USERNAME"),
    EXPLAIN_GIT_PWD("EXPLAIN_GIT_PWD", "EXPLAIN_GIT_PWD");

    private String itemKey;
    private String itemDesc;

    ExplainCdlItemEnum(String itemKey, String itemDesc) {
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
        for (ExplainCdlItemEnum itemEnum : ExplainCdlItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}