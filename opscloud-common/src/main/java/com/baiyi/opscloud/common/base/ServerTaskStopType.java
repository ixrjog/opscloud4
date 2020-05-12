package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/4/8 3:44 下午
 * @Version 1.0
 */
public enum ServerTaskStopType {

    DEFAULT(0),
    // 正常完成停止任务
    COMPLETE_STOP(1),
    // 超时停止任务
    TIMEOUT_STOP(2),
    // 子任务强制停止
    MEMBER_TASK_STOP(3),
    // 主任务强制停止
    SERVER_TASK_STOP(4),
    LOG_EXCEEDED_LIMIT(5);

    private int type;

    ServerTaskStopType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
