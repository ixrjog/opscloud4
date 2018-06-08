package com.sdg.cmdb.domain.server;

/**
 * Created by zxxiao on 2016/12/20.
 */
public enum EnvTypeEnum {
    keep(0, "保留"),
    dev(1, "dev"),
    daily(2, "daily"),
    gray(3, "gray"),
    prod(4, "prod"),
    test(5, "test");
    private int code;
    private String desc;

    EnvTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(int code) {
        for(EnvTypeEnum typeEnum : EnvTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum.getDesc();
            }
        }
        return "未定义";
    }
}
