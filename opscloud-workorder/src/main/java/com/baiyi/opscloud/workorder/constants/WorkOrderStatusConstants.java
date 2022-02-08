package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/2/8 10:41 AM
 * @Version 1.0
 */
@Getter
public enum WorkOrderStatusConstants {

    NORMAL(0), // 正常
    DEVELOPING(1), // 开发中
    SYS(2), // 系统工单
    INACTIVE(3) //停用
    ;

    WorkOrderStatusConstants(int status) {
        this.status = status;
    }

    private final int status;

}
