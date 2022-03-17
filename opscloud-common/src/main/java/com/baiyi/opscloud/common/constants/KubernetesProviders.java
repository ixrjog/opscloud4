package com.baiyi.opscloud.common.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/3/17 09:38
 * @Version 1.0
 */
public enum KubernetesProviders {

    AMAZON_EKS("AmazonEKS");

    @Getter
    private final String desc;

    KubernetesProviders(String desc) {
        this.desc = desc;
    }

}
