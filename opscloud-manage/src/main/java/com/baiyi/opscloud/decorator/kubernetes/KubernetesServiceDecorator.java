package com.baiyi.opscloud.decorator.kubernetes;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesServicePort;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesClusterVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesServiceVO;
import com.baiyi.opscloud.facade.kubernetes.BaseKubernetesFacade;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesServicePortService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 2:09 下午
 * @Version 1.0
 */
@Component
public class KubernetesServiceDecorator extends BaseKubernetesFacade {

    @Resource
    private OcKubernetesServicePortService ocKubernetesServicePortService;

    public KubernetesServiceVO.Service decorator(KubernetesServiceVO.Service service, Integer extend) {
        if (extend == 1) {
            service.setPorts(getPorts(service.getId()));
            OcKubernetesCluster ocKubernetesCluster = getOcKubernetesClusterByNamespaceId(service.getNamespaceId());
            service.setCluster(BeanCopierUtils.copyProperties(ocKubernetesCluster, KubernetesClusterVO.Cluster.class));
            invokeBaseProperty(service);
        }
        return service;
    }

    private List<KubernetesServiceVO.Port> getPorts(int serviceId) {
        List<OcKubernetesServicePort> portList = ocKubernetesServicePortService.queryOcKubernetesServicePortByServiceId(serviceId);
        if (CollectionUtils.isEmpty(portList)) return Lists.newArrayList();
        return BeanCopierUtils.copyListProperties(portList, KubernetesServiceVO.Port.class);
    }
}
