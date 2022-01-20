package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * 处理状态
 * @Author baiyi
 * @Date 2022/1/7 1:33 PM
 * @Version 1.0
 */
public enum ProcessStatusConstants {

    DEFAULT(0),
    SUCCESSFUL(1), // 执行成功
    FAILED(-1);  // 执行失败

    ProcessStatusConstants(int status) {
        this.status = status;
    }

    @Getter
    private final int status;
}
