package com.baiyi.opscloud.common.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/3/17 09:38
 * @Version 1.0
 */
@Getter
public enum KubernetesProviders {

    /**
     * 供应商
     */
    AMAZON_EKS("AmazonEKS");

    private final String desc;

    KubernetesProviders(String desc) {
        this.desc = desc;
    }

}