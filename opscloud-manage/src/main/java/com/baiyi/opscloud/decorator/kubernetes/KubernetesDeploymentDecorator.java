package com.baiyi.opscloud.decorator.kubernetes;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesClusterVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesDeploymentVO;
import com.baiyi.opscloud.facade.kubernetes.BaseKubernetesFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * @Author baiyi
 * @Date 2020/6/29 4:16 下午
 * @Version 1.0
 */
@Component
public class KubernetesDeploymentDecorator extends BaseKubernetesFacade {

    public KubernetesDeploymentVO.Deployment decorator(KubernetesDeploymentVO.Deployment deployment, Integer extend) {
        if (extend == 1) {
            try {
                Deployment kubernetesDeployment =
                        new ObjectMapper().readValue(deployment.getDeploymentDetail(), io.fabric8.kubernetes.api.model.apps.Deployment.class);
                deployment.setDeployment(kubernetesDeployment);

                Yaml yaml = new Yaml();
                deployment.setDeploymentYAML(yaml.dumpAs(kubernetesDeployment, Tag.MAP, DumperOptions.FlowStyle.BLOCK));
            } catch (Exception e) {
                e.printStackTrace();
            }
            OcKubernetesCluster ocKubernetesCluster = getOcKubernetesClusterByNamespaceId(deployment.getNamespaceId());
            deployment.setCluster(BeanCopierUtils.copyProperties(ocKubernetesCluster, KubernetesClusterVO.Cluster.class));
            invokeBaseProperty(deployment);
        }
        return deployment;
    }


}
