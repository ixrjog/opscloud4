package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * 处理状态
 * @Author baiyi
 * @Date 2022/1/7 1:33 PM
 * @Version 1.0
 */
@Getter
public enum ProcessStatusConstants {

    /**
     * 处理状态
     */
    DEFAULT(0),
    // 执行成功
    SUCCESSFUL(1),
    // 执行失败
    FAILED(-1);

    ProcessStatusConstants(int status) {
        this.status = status;
    }

    private final int status;

}