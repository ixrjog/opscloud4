package com.baiyi.opscloud.common.constants.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2021/6/9 10:32 上午
 * @Version 1.0
 */
@Getter
public enum UserCredentialTypeEnum {

    PUB_KEY(0, "PUB_KEY"),
    API_TOKEN(1, "API_TOKEN");

    private final int type;
    private final String name;

    UserCredentialTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        return Arrays.stream(UserCredentialTypeEnum.values()).filter(typeEnum -> typeEnum.getType() == type).findFirst().map(UserCredentialTypeEnum::getName).orElse("undefined");
    }
}
