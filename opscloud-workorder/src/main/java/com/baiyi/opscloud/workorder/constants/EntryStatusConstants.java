package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/1/7 1:33 PM
 * @Version 1.0
 */
public enum EntryStatusConstants {

    DEFAULT(0),
    SUCCESSFUL(1),
    FAILED(-1);

    EntryStatusConstants(int status) {
        this.status = status;
    }

    @Getter
    private final int status;
}
