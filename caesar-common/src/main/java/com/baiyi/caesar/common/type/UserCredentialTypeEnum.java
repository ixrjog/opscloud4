package com.baiyi.caesar.common.type;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/6/9 10:32 上午
 * @Version 1.0
 */
@Getter
public enum UserCredentialTypeEnum {

    PUB_KEY(0, "PUB_KEY"),
    API_TOKEN(1, "API_TOKEN");

    private int type;
    private String name;


    UserCredentialTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        for (UserCredentialTypeEnum typeEnum : UserCredentialTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum.getName();
            }
        }
        return "undefined";
    }
}
