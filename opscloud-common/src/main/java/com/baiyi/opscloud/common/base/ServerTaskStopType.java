package com.baiyi.opscloud.common.base;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2020/4/8 3:44 下午
 * @Version 1.0
 */
@Getter
public enum ServerTaskStopType {

    /**
     * 默认
     */
    DEFAULT(0),
    /**
     * 正常完成停止任务
     */
    COMPLETE_STOP(1),
    /**
     * 超时停止任务
     */
    TIMEOUT_STOP(2),
    /**
     * 子任务强制停止
     */
    MEMBER_TASK_STOP(3),
    /**
     * 主任务强制停止
     */
    SERVER_TASK_STOP(4),
    LOG_EXCEEDED_LIMIT(5),
    ERROR_STOP(6);

    private final int type;

    ServerTaskStopType(int type) {
        this.type = type;
    }

}