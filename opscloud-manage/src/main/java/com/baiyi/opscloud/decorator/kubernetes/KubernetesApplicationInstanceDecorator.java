package com.baiyi.opscloud.decorator.kubernetes;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesApplicationVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesDeploymentVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesServiceVO;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesApplicationService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesDeploymentService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesServiceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/2 2:23 下午
 * @Version 1.0
 */
@Component
public class KubernetesApplicationInstanceDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcKubernetesDeploymentService ocKubernetesDeploymentService;

    @Resource
    private OcKubernetesServiceService ocKubernetesServiceService;

    @Resource
    private OcKubernetesApplicationService ocKubernetesApplicationService;

    public KubernetesApplicationVO.Instance decorator(OcKubernetesApplicationInstance ocKubernetesApplicationInstance, Integer extend) {
        KubernetesApplicationVO.Instance instance = BeanCopierUtils.copyProperties(ocKubernetesApplicationInstance, KubernetesApplicationVO.Instance.class);
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocKubernetesApplicationInstance.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
            instance.setEnv(env);
        }

        OcKubernetesDeployment ocKubernetesDeployment = ocKubernetesDeploymentService.queryOcKubernetesDeploymentByInstanceId(instance.getId());
        if (ocKubernetesDeployment != null)
            instance.setDeployment(BeanCopierUtils.copyProperties(ocKubernetesDeployment, KubernetesDeploymentVO.Deployment.class));

        OcKubernetesService ocKubernetesService = ocKubernetesServiceService.queryOcKubernetesServiceByInstanceId(instance.getId());
        if (ocKubernetesService != null)
            instance.setService(BeanCopierUtils.copyProperties(ocKubernetesService, KubernetesServiceVO.Service.class));

        if (extend == 1) {
            OcKubernetesApplication ocKubernetesApplication = ocKubernetesApplicationService.queryOcKubernetesApplicationById(instance.getApplicationId());
            instance.setApplication(BeanCopierUtils.copyProperties(ocKubernetesApplication, KubernetesApplicationVO.Application.class));
        }

        return instance;
    }
}
