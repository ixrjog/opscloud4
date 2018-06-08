package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum ShadowsocksItemEnum {
    SHADOWSOCKS_SERVER_1("SHADOWSOCKS_SERVER_1", "翻墙代理服务器1"),
    SHADOWSOCKS_SERVER_2("SHADOWSOCKS_SERVER_2", "翻墙代理服务器2");

    private String itemKey;
    private String itemDesc;

    ShadowsocksItemEnum(String itemKey, String itemDesc) {
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
        for (ShadowsocksItemEnum itemEnum : ShadowsocksItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }

}