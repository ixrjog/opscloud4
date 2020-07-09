package com.baiyi.opscloud.builder.kubernetes;

import com.baiyi.opscloud.bo.kubernetes.KubernetesContainerBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesPodVO;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/4 4:20 下午
 * @Version 1.0
 */
public class KubernetesContainerBuilder {

    public static KubernetesPodVO.Container build(Pod pod, Container container) {
        ContainerStatus containerStatus = getContainerStatus(pod, container.getName());
        return containerStatus == null ? build(container): build(container,containerStatus);
    }

    private static KubernetesPodVO.Container build(Container container) {
        KubernetesContainerBO bo = KubernetesContainerBO.builder()
                .image(container.getImage())
                .name(container.getName())
                .build();
        return covert(bo);
    }

    private static KubernetesPodVO.Container build(Container container, ContainerStatus containerStatus) {
        KubernetesContainerBO bo = KubernetesContainerBO.builder()
                .image(container.getImage())
                .name(container.getName())
                .containerID(containerStatus.getContainerID())
                .imageID(containerStatus.getImageID())
                .ready(containerStatus.getReady())
                .started(containerStatus.getStarted())
                .id(StringUtils.isEmpty(containerStatus.getContainerID()) ? "" : containerStatus.getContainerID().substring(9, 21))
                .build();
        return covert(bo);
    }

    private static ContainerStatus getContainerStatus(Pod pod, String containerName) {
        List<ContainerStatus> list = pod.getStatus().getContainerStatuses().stream().filter(e -> containerName.equals(e.getName())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list))
            return null;
        return list.get(0);
    }

    private static KubernetesPodVO.Container covert(KubernetesContainerBO bo) {
        return BeanCopierUtils.copyProperties(bo, KubernetesPodVO.Container.class);
    }
}
