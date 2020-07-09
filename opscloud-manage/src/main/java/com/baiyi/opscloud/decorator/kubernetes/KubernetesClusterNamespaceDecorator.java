package com.baiyi.opscloud.decorator.kubernetes;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesClusterNamespaceVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesClusterVO;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/28 4:42 下午
 * @Version 1.0
 */
@Component
public class KubernetesClusterNamespaceDecorator {

    @Resource
    private OcKubernetesClusterService ocKubernetesClusterService;

    @Resource
    private OcEnvService ocEnvService;

    public KubernetesClusterNamespaceVO.Namespace decorator(KubernetesClusterNamespaceVO.Namespace namespace, Integer extend) {
        if (extend == 1) {
            OcKubernetesCluster ocKubernetesCluster = ocKubernetesClusterService.queryOcKubernetesClusterById(namespace.getClusterId());
            namespace.setCluster(BeanCopierUtils.copyProperties(ocKubernetesCluster, KubernetesClusterVO.Cluster.class));

            // 装饰 环境信息
            OcEnv ocEnv = ocEnvService.queryOcEnvByType(namespace.getEnvType());
            if (ocEnv != null) {
                EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
                namespace.setEnv(env);
            }
        }
        return namespace;
    }
}
