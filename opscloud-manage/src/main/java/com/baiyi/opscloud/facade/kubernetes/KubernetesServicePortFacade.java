package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.builder.kubernetes.KubernetesServicePortBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesService;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesServicePort;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesServicePortService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ServicePort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/1 11:10 上午
 * @Version 1.0
 */
@Service
public class KubernetesServicePortFacade {

    @Resource
    private OcKubernetesServicePortService ocKubernetesServicePortService;

    public void saveKubernetesServicePort(OcKubernetesService ocKubernetesService, io.fabric8.kubernetes.api.model.Service service) {
        Map<String, OcKubernetesServicePort> portMap = getPortMap(ocKubernetesService.getId());
        if (service.getSpec() != null && service.getSpec().getPorts() != null && !CollectionUtils.isEmpty(service.getSpec().getPorts())) {
            service.getSpec().getPorts().forEach(e -> saveKubernetesServicePort(portMap, ocKubernetesService, e));
        }
        delKubernetesServicePortByMap(portMap);
    }

    private void saveKubernetesServicePort(Map<String, OcKubernetesServicePort> serviceMap, OcKubernetesService ocKubernetesService, ServicePort servicePort) {
        if (StringUtils.isEmpty(servicePort.getName())) return;
        OcKubernetesServicePort pre = KubernetesServicePortBuilder.build(ocKubernetesService, servicePort);
        OcKubernetesServicePort ocKubernetesServicePort = serviceMap.get(pre.getName());
        if (ocKubernetesServicePort == null) {
            ocKubernetesServicePortService.addOcKubernetesServicePort(pre);
        } else {
            serviceMap.remove(pre.getName());
            pre.setId(ocKubernetesServicePort.getId());
            ocKubernetesServicePortService.updateOcKubernetesServicePort(pre);
        }
    }

    private Map<String, OcKubernetesServicePort> getPortMap(int serviceId) {
        List<OcKubernetesServicePort> ports = ocKubernetesServicePortService.queryOcKubernetesServicePortByServiceId(serviceId);
        if (CollectionUtils.isEmpty(ports)) return Maps.newHashMap();
        return ports.stream().collect(Collectors.toMap(OcKubernetesServicePort::getName, a -> a, (k1, k2) -> k1));
    }

    private void delKubernetesServicePortByMap(Map<String, OcKubernetesServicePort> portMap) {
        if (portMap.isEmpty()) return;
        portMap.keySet().forEach(k -> ocKubernetesServicePortService.deleteOcKubernetesServicePortById(portMap.get(k).getId()));
    }

    public void delKubernetesServicePortByServiceId(int id) {
        ocKubernetesServicePortService.queryOcKubernetesServicePortByServiceId(id).forEach(e -> ocKubernetesServicePortService.deleteOcKubernetesServicePortById(e.getId())
        );
    }

}
