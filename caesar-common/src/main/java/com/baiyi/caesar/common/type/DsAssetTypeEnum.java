package com.baiyi.caesar.common.type;

/**
 * @Author baiyi
 * @Date 2021/6/21 11:18 上午
 * @Version 1.0
 */
public enum DsAssetTypeEnum {

    GROUP("GROUP"),
    USER("USER"),
    ECS("ECS"),
    GITLAB_GROUP("GITLAB_GROUP"),
    GITLAB_PROJECT("GITLAB_PROJECT"),

    ;

    private String type;

    DsAssetTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
