package com.baiyi.opscloud.kubernetes.confg;

import lombok.Data;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/6/28 6:07 下午
 * @Version 1.0
 */
@Data
public class KubernetesNamespaceConfig {

    private Set<String> filter;
}
