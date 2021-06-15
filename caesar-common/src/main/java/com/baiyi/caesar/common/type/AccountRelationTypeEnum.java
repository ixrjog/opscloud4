package com.baiyi.caesar.common.type;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/6/15 1:32 下午
 * @Version 1.0
 */
@Getter
public enum AccountRelationTypeEnum {

    ACCOUNT_GROUP(0, "ACCOUNT_GROUP");

    private int type;
    private String name;

    AccountRelationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        for (AccountRelationTypeEnum typeEnum : AccountRelationTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum.getName();
            }
        }
        return "undefined";
    }
}