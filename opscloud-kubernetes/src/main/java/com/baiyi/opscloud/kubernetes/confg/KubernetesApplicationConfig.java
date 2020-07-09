package com.baiyi.opscloud.kubernetes.confg;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/2 3:37 下午
 * @Version 1.0
 */
@Data
public class KubernetesApplicationConfig {

    private Map<String, List<String>> envLabel;
}
