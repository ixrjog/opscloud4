package com.baiyi.caesar.common.type;

/**
 * @Author baiyi
 * @Date 2021/5/6 1:22 下午
 * @Version 1.0
 */
public enum ApplicationResEnum {

    SERVER_GROUP("SERVER_GROUP"),
    GITLAB_PROJECT("GITLAB_PROJECT"),
    GITLAB_GROUP("GITLAB_GROUP");

    private String type;

    ApplicationResEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
