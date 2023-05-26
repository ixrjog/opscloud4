package com.baiyi.opscloud.alert.notify;

import lombok.Getter;

/**
 * @Author 修远
 * @Date 2022/7/29 7:21 PM
 * @Since 1.0
 */
@Getter
public enum NotifyStatusEnum {

    /**
     * 通知
     */
    CALL_OK("用户接听"),
    CALL_ERR("用户未接听");

    private final String name;

    NotifyStatusEnum(String name) {
        this.name = name;
    }

}
