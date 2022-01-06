package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/10/21 2:36 下午
 * @Version 1.0
 */
public enum NodeTypeConstants {

    USER_SELECTION(0);

    NodeTypeConstants(int code) {
        this.code = code;
    }

    @Getter
    private final int code;


}
