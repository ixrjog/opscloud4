package com.baiyi.opscloud.workorder.constants;

/**
 * 工单状态码
 * @Author baiyi
 * @Date 2021/11/11 11:32 上午
 * @Version 1.0
 */
public enum OrderStatusCodeConstants {

    NEW, //新建
    TOAUDIT, //审批中
    APPROVED, //审批通过
    REJECT,//审批拒绝
    PROCESSING,//执行中
    SUCCESS,//执行成功
    CLOSED //已关闭

}
