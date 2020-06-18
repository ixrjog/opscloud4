package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/6/1 11:27 上午
 * @Version 1.0
 */
public enum CloudServerPowerStatus {

    STOPPED(0), // 实例完全停止关机的稳定状态
    RUNNING(1), // 在ECS控制台或者使用接口StartInstance成功开启实例后，实例的稳定运行状态。这是实例的正常状态，实例拥有者此时可以运行、管理或者调整实例上运行的业务或者应用。

    STARTING(2), // 在ECS控制台或者使用接口StartInstance开启实例后，实例的瞬时状态。
    STOPPING(3), // 在ECS控制台或者使用接口StopInstance停止实例后，实例的瞬时状态。
    PENDING(4) // 在ECS控制台或者使用接口RunInstances创建实例后，实例的默认状态。Pending状态只出现在实例创建时刻，持续时间为秒为计时单位。
    ;

    private int status;

    CloudServerPowerStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
