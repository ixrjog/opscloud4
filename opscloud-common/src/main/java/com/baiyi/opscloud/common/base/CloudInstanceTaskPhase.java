package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/4/1 10:21 上午
 * @Version 1.0
 */
public enum CloudInstanceTaskPhase {

    CREATE_INSTANCE("CREATE_INSTANCE"),
    ALLOCATE_PUBLIC_IP_ADDRESS("ALLOCATE_PUBLIC_IP_ADDRESS"),
    STARTING("STARTING"),
    RUNNING("RUNNING"),
    CLOUD_SERVER_RECORDED("CLOUD_SERVER_RECORDED"),
    SERVER_RECORDED("SERVER_RECORDED"),
    FINALIZED("FINALIZED");

    private String phase;

    CloudInstanceTaskPhase(String phase) {
        this.phase = phase;
    }

    public String getPhase() {
        return this.phase;
    }
}
