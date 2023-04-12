package com.baiyi.opscloud.leo.helper;

import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.packer.PodDetailsPacker;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/12/13 13:29
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PodDetailsHelper {

    private final PodDetailsPacker podDetailsPacker;

    public LeoDeployingVO.PodDetails toPodDetails(Pod pod, String containerName) {
        // 容器状态
        Optional<ContainerStatus> optionalContainerStatus = pod.getStatus().getContainerStatuses().stream().filter(e -> e.getName().equals(containerName)).findFirst();

        Date startTime = com.baiyi.opscloud.core.util.TimeUtil.toDate(pod.getStatus().getStartTime(), TimeZoneEnum.UTC);
        LeoDeployingVO.PodDetails podDetails = LeoDeployingVO.PodDetails.builder()
                .podIP(pod.getStatus().getPodIP())
                .hostIP(pod.getStatus().getHostIP())
                .phase(pod.getStatus().getPhase())
                .reason(pod.getStatus().getReason())
                .name(pod.getMetadata().getName())
                .namespace(pod.getMetadata().getNamespace())
                .terminating(optionalContainerStatus.isPresent() && optionalContainerStatus.get().getState().getTerminated() != null)
                .startTime(startTime)
                .conditions(pod.getStatus().getConditions().stream().collect(Collectors.toMap(PodCondition::getType, PodCondition::getStatus, (k1, k2) -> k1)))
                .restartCount(optionalContainerStatus.isPresent() ? optionalContainerStatus.get().getRestartCount() : 0)
                .build()
                .init();
        podDetailsPacker.wrap(podDetails);
        return podDetails;
    }

}