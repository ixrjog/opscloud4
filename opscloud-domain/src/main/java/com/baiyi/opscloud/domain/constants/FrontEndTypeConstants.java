package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/9/26 11:26
 * @Version 1.0
 */
public enum FrontEndTypeConstants {

    SUCCESS("success"),
    INFO("info"),
    WARNING("warning"),
    DANGER("danger");

    @Getter
    private final String type;

    FrontEndTypeConstants(String type) {
        this.type = type;
    }
}
