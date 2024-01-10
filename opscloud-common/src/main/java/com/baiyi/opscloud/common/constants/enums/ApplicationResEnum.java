package com.baiyi.opscloud.common.constants.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/5/6 1:22 下午
 * @Version 1.0
 */
@Getter
public enum ApplicationResEnum {

    /**
     * 资源类型
     */
    SERVER_GROUP("SERVER_GROUP"),
    GITLAB_PROJECT("GITLAB_PROJECT"),
    GITLAB_GROUP("GITLAB_GROUP");

    private final String type;

    ApplicationResEnum(String type) {
        this.type = type;
    }

}