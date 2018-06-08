package com.sdg.cmdb.domain.zabbix;

/**
 * Created by zxxiao on 2017/2/17.
 */
public enum HistoryTypeEnum {
    //0 保留
    cpuUser(0, "cpu使用率"),
    perCpuAvg(1, "load值"),
    daily(2, "daily"),
    gray(3, "gray"),
    prod(4, "production"),
    test(5, "test"),
    back(6, "back");
    private int code;
    private String desc;

    HistoryTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getHistoryTypeName(int code) {
        for (HistoryTypeEnum historyTypeEnum : HistoryTypeEnum.values()) {
            if (historyTypeEnum.getCode() == code) {
                return historyTypeEnum.getDesc();
            }
        }
        return "undefined";
    }
}