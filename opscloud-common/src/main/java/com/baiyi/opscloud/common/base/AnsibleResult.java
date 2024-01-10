package com.baiyi.opscloud.common.base;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2020/4/13 9:09 上午
 * @Version 1.0
 */
@Getter
public enum AnsibleResult {

    /**
     * 结果
     */
    SUCCESSFUL(0, "SUCCESSFUL"),
    FAILED(1, "FAILED");


    private final int type;
    private final String name;

    AnsibleResult(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        return Arrays.stream(AnsibleResult.values()).filter(result -> result.getType() == type).findFirst().map(AnsibleResult::getName).orElse("FAILED");
    }

}