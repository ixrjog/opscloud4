package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/10/21 2:36 下午
 * @Version 1.0
 */
@Getter
public enum NodeTypeConstants {

    /**
     * USER_LIST：用户自定义
     * SYS: 系统定义
     */
    USER_LIST(0),
    SYS(1);

    NodeTypeConstants(int code) {
        this.code = code;
    }

    private final int code;

}