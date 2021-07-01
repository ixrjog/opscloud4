package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/6/30 10:51 上午
 * @Version 1.0
 */
public enum KubernetesTPLType {

    SERVICE("SERVICE"), // 服务模版
    DEPLOYMENT("DEPLOYMENT");  // 无状态模版

    private String type;

    KubernetesTPLType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
