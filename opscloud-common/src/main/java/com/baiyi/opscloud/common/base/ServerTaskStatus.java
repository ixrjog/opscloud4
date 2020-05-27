package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/4/8 3:08 下午
 * @Version 1.0
 */
public enum ServerTaskStatus {

    QUEUE("QUEUE"),
    // 执行队列，还未执行
    EXECUTE_QUEUE("EXECUTE_QUEUE"),
    EXECUTING("EXECUTING"),
    FINALIZED("FINALIZED");

    private String status;

    ServerTaskStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
