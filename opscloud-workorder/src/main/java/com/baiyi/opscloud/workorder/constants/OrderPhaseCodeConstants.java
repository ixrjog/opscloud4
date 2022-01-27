package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * 工单状态码
 *
 * @Author baiyi
 * @Date 2021/11/11 11:32 上午
 * @Version 1.0
 */
@Getter
public enum OrderPhaseCodeConstants {

    NEW("NEW", "新建"),
    TOAUDIT("TOAUDIT", "工单申请中！"),
    APPROVED("APPROVED", "工单审批通过！"),
    REJECT("REJECT", "工单审批被拒绝！"),
    PROCESSING("PROCESSING", "工单执行中！"),
    SUCCESS("SUCCESS", "工单执行成功！"),
    FAILED("FAILED", "工单执行失败！"),
    CLOSED("CLOSED", "工单已关闭");

    private String phase;
    private String result;

    OrderPhaseCodeConstants(String phase, String result) {
        this.phase = phase;
        this.result = result;
    }

    public static OrderPhaseCodeConstants getEnum(String phase) {
        for (OrderPhaseCodeConstants phaseCodeConstants : OrderPhaseCodeConstants.values()) {
            if (phaseCodeConstants.getPhase().equals(phase)) {
                return phaseCodeConstants;
            }
        }
        return null;
    }
}
