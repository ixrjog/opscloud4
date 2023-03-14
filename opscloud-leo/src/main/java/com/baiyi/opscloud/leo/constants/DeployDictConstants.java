package com.baiyi.opscloud.leo.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/12/8 11:24
 * @Version 1.0
 */
public enum DeployDictConstants {

    /**
     * 部署字典
     */
    DEPLOY_NUMBER("deployNumber"),
    DEPLOY_TYPE("deployType"),
    DEPLOY_TYPE_DESC("deployTypeDesc"),
    DISPLAY_NAME("displayName")
    ;


    @Getter
    private final String key;

    DeployDictConstants(String key) {
        this.key = key;
    }
}
