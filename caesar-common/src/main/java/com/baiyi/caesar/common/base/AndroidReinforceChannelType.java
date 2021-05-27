package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2020/8/27 2:06 下午
 * @Version 1.0
 */
public enum AndroidReinforceChannelType {

    YYB("yyb", "腾讯应用宝"),
    HUAWEI("huawei", "华为"),
    XIAOMI("xiaomi", "小米"),
    OPPO("oppo", "oppo手机"),
    VIVO("vivo", "vivo");

    private String code;
    private String desc;

    AndroidReinforceChannelType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getName(String code) {
        for (AndroidReinforceChannelType channelType : AndroidReinforceChannelType.values()) {
            if (channelType.getCode().equals(code)) {
                return channelType.getDesc();
            }
        }
        return "undefined";
    }
}
