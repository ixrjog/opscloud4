package com.baiyi.opscloud.sshcore.util;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.sshcore.model.KubernetesResource;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/7/21 5:55 下午
 * @Version 1.0
 */
public class TerminalSessionUtil {

    /**
     * 默认容器
     */
    private static final String DEFAULT_CONTAINER = "DEFAULT_CONTAINER";

    public static String toInstanceId(KubernetesResource.Pod pod, KubernetesResource.Container container) {
        return toInstanceId(pod.getName(), container.getName());
    }

    public static String toInstanceId(String podName, String containerName) {
        if (StringUtils.isEmpty(containerName)) {
            containerName = DEFAULT_CONTAINER;
        }
        return Joiner.on("#").join(podName, containerName, IdUtil.buildUUID());
    }

}