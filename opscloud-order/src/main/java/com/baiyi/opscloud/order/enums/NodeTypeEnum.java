package com.baiyi.opscloud.order.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/10/21 2:36 下午
 * @Version 1.0
 */
public enum NodeTypeEnum {

    USER_SELECTION(0);

    NodeTypeEnum(int code) {
        this.code = code;
    }

    @Getter
    private int code;


}
