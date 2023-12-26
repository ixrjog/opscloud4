package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * 工单审批动作
 *
 * @Author baiyi
 * @Date 2021/11/11 11:23 上午
 * @Version 1.0
 */
@Getter
public enum ApprovalTypeConstants {

    /**
     * 动作
     */
    AGREE("同意"),
    CANCEL("取消"),
    REJECT("拒绝");

    ApprovalTypeConstants(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public static String getDesc(String name) {
        return Arrays.stream(ApprovalTypeConstants.values()).filter(typeEnum -> typeEnum.name().equals(name)).findFirst().map(ApprovalTypeConstants::getDesc).orElse("undefined");
    }

}