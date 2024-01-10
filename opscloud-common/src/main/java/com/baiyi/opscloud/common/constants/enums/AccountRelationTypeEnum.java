package com.baiyi.opscloud.common.constants.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2021/6/15 1:32 下午
 * @Version 1.0
 */
@Getter
public enum AccountRelationTypeEnum {

    /**
     *
     */
    ACCOUNT_GROUP(0, "ACCOUNT_GROUP");

    private final int type;
    private final String name;

    AccountRelationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String of(int type) {
        return Arrays.stream(AccountRelationTypeEnum.values()).filter(typeEnum -> typeEnum.getType() == type).findFirst().map(AccountRelationTypeEnum::getName).orElse("undefined");
    }

}