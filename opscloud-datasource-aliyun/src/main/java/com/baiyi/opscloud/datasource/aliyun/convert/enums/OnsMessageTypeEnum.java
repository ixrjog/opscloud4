package com.baiyi.opscloud.datasource.aliyun.convert.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/9/30 3:46 下午
 * @Version 1.0
 */
public enum OnsMessageTypeEnum {

    TYPE_0(0, "普通消息"),
    TYPE_1(1, "分区顺序消息"),
    TYPE_2(2, "全局顺序消息"),
    TYPE_4(4, "事务消息"),
    TYPE_5(5, "定时/延时消息");

    @Getter
    private int type;
    @Getter
    private String desc;

    OnsMessageTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getDesc(int type) {
        for (OnsMessageTypeEnum typeEnum : OnsMessageTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum.getDesc();
            }
        }
        return "undefined";
    }

}
