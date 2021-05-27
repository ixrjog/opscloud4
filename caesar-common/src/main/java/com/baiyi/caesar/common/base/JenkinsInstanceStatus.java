package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2020/8/3 9:32 上午
 * @Version 1.0
 */
public enum JenkinsInstanceStatus {

    RUNNING(0), // 运行中
    MAINTAINING(1), // 维护中
    UPGRADES(2) // 升级
    ;

    private int status;

    JenkinsInstanceStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
