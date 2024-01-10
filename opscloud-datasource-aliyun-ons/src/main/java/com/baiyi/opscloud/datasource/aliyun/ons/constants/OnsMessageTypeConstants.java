package com.baiyi.opscloud.datasource.aliyun.ons.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2021/9/30 3:46 下午
 * @Version 1.0
 */
@Getter
public enum OnsMessageTypeConstants {

    /**
     * 消息类型
     */
    TYPE_0(0, "普通消息"),
    TYPE_1(1, "分区顺序消息"),
    TYPE_2(2, "全局顺序消息"),
    TYPE_4(4, "事务消息"),
    TYPE_5(5, "定时/延时消息");

    private final int type;
    private final String desc;

    OnsMessageTypeConstants(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getDesc(int type) {
        return Arrays.stream(OnsMessageTypeConstants.values())
                .filter(typeEnum -> typeEnum.getType() == type)
                .findFirst()
                .map(OnsMessageTypeConstants::getDesc)
                .orElse("undefined");
    }

}