package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/4/1 1:43 下午
 * @Version 1.0
 */
public enum CloudInstanceTaskStatus {

    // 不稳定
    UNSTABLE("UNSTABLE"),
    // 失败
    FAILURE("FAILURE"),
    // 完成
    COMPLETED("COMPLETED");

    private String status;

    CloudInstanceTaskStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
