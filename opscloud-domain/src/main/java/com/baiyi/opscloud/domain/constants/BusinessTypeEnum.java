package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2020/2/22 5:46 下午
 * @Version 1.0
 */
@Getter
public enum BusinessTypeEnum {
    COMMON(0, "通用"),
    SERVER(1, "服务器", true),
    SERVERGROUP(2, "服务器组", true),
    USER(3, "用户"),
    USERGROUP(4, "用户组"),
    ASSET(5, "资产", true),
    /**
     * 服务器器管理员账户
     **/
    SERVER_ADMINISTRATOR_ACCOUNT(6, "SERVER_ADMINISTRATOR_ACCOUNT"),

    /**
     * 应用授权
     **/
    APPLICATION(8, "APPLICATION"),
    DATASOURCE_INSTANCE(16, "数据源实例"),
    USER_PERMISSION(100, "USER_PERMISSION"),
    BUSINESS_ASSET_RELATION(101,"BUSINESS_ASSET_RELATION")
    ;

    private final String name;
    private final int type;
    private final boolean inApplication;

    public static BusinessTypeEnum getByType(int type) {
        return Arrays.stream(BusinessTypeEnum.values()).filter(typeEnum -> typeEnum.type == type).findFirst().orElse(null);
    }

    BusinessTypeEnum(int type, String name, boolean inApplication) {
        this.type = type;
        this.name = name;
        this.inApplication = inApplication;
    }

    BusinessTypeEnum(int type, String name) {
        this(type, name, false);
    }
}
