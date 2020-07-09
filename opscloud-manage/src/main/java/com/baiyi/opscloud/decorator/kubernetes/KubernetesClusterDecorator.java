package com.baiyi.opscloud.decorator.kubernetes;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesClusterNamespace;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesClusterNamespaceVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesClusterVO;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterNamespaceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/28 4:33 下午
 * @Version 1.0
 */
@Component
public class KubernetesClusterDecorator {

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    public KubernetesClusterVO.Cluster decorator(KubernetesClusterVO.Cluster cluster, Integer extend) {
        if (extend == 1) {
            List<OcKubernetesClusterNamespace> list = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceByClusterId(cluster.getId());
            cluster.setNamespaces(BeanCopierUtils.copyListProperties(list, KubernetesClusterNamespaceVO.Namespace.class));
        }
        return cluster;
    }

}
