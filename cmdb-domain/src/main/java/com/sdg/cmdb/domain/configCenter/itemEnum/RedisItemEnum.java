package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum RedisItemEnum {
    REDIS_HOST("REDIS_HOST", "redis主机"),
    REDIS_PORT("REDIS_PORT", "端口"),
    REDIS_PASSWD("REDIS_PASSWD", "密码");


    private String itemKey;
    private String itemDesc;

    RedisItemEnum(String itemKey, String itemDesc) {
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
        for (RedisItemEnum itemEnum : RedisItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}