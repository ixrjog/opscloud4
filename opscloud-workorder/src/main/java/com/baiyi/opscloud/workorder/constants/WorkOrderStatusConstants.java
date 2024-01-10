package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/2/8 10:41 AM
 * @Version 1.0
 */
@Getter
public enum WorkOrderStatusConstants {

    /**
     * 正常
     */
    NORMAL(0),
    /**
     * 开发中
     */
    DEVELOPING(1),
    /**
     * 系统工单
     */
    SYS(2),
    /**
     * 停用
     */
    INACTIVE(3);


    WorkOrderStatusConstants(int status) {
        this.status = status;
    }

    private final int status;

}