package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.kubernetes.KubernetesApplicationInstanceDecorator;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesApplication;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesApplicationInstance;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesClusterNamespace;
import com.baiyi.opscloud.domain.vo.kubernetes.BaseKubernetesApplicationVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesApplicationVO;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesApplicationService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterNamespaceService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/1 11:01 上午
 * @Version 1.0
 */
@Component
public class  BaseKubernetesFacade {

    @Resource
    private OcKubernetesClusterService ocKubernetesClusterService;

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    @Resource
    private OcKubernetesApplicationService ocKubernetesApplicationService;

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private KubernetesApplicationInstanceDecorator kubernetesApplicationInstanceDecorator;

    protected OcKubernetesCluster getOcKubernetesCluster(OcKubernetesClusterNamespace ocKubernetesClusterNamespace) {
        return ocKubernetesClusterService.queryOcKubernetesClusterById(ocKubernetesClusterNamespace.getClusterId());
    }

    protected OcKubernetesCluster getOcKubernetesClusterByNamespaceId(int namespaceId) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceById(namespaceId);
        return ocKubernetesClusterService.queryOcKubernetesClusterById(ocKubernetesClusterNamespace.getClusterId());
    }


    protected void invokeBaseProperty(BaseKubernetesApplicationVO.BaseProperty baseProperty){
        if ( baseProperty.getApplicationId() != 0){
            OcKubernetesApplication ocKubernetesApplication =  ocKubernetesApplicationService.queryOcKubernetesApplicationById(baseProperty.getApplicationId());
            baseProperty.setApplication(BeanCopierUtils.copyProperties(ocKubernetesApplication,KubernetesApplicationVO.Application.class));
        }
        if (baseProperty.getInstanceId() != 0){
            OcKubernetesApplicationInstance ocKubernetesApplicationInstance =  ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(baseProperty.getInstanceId());
            baseProperty.setInstance(kubernetesApplicationInstanceDecorator.decorator(ocKubernetesApplicationInstance,0));
        }
    }

}
