package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/2/14 11:16 AM
 * @Version 1.0
 */
@Getter
public enum OrderTicketStatusConstants {

    /**
     * 工单状态 0 正常  1 结束（成功） 2结束（失败）
     */
    NORMAL(0),
    SUCCESS(1),
    FAILED(2);

    private final int status;

    OrderTicketStatusConstants(int status) {
        this.status = status;
    }

}