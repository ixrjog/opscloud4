package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.builder.kubernetes.KubernetesDeploymentBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.IDUtils;
import com.baiyi.opscloud.decorator.kubernetes.KubernetesDeploymentDecorator;
import com.baiyi.opscloud.decorator.kubernetes.KubernetesTemplateDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesDeploymentParam;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesDeploymentVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesTemplateVO;
import com.baiyi.opscloud.kubernetes.confg.KubernetesConfig;
import com.baiyi.opscloud.kubernetes.handler.KubernetesDeploymentHandler;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterNamespaceService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesDeploymentService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesTemplateService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/7/1 11:03 上午
 * @Version 1.0
 */
@Service
public class KubernetesDeploymentFacade extends BaseKubernetesFacade {

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    @Resource
    private KubernetesDeploymentHandler kubernetesDeploymentHandler;

    @Resource
    private OcKubernetesDeploymentService ocKubernetesDeploymentService;

    @Resource
    private KubernetesDeploymentDecorator kubernetesDeploymentDecorator;

    @Resource
    private KubernetesTemplateDecorator kubernetesTemplateDecorator;

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private OcKubernetesTemplateService ocKubernetesTemplateService;

    @Resource
    private KubernetesConfig kubernetesConfig;

    public DataTable<KubernetesDeploymentVO.Deployment> queryKubernetesDeploymentPage(KubernetesDeploymentParam.PageQuery pageQuery) {
        DataTable<OcKubernetesDeployment> table = ocKubernetesDeploymentService.queryOcKubernetesDeploymentByParam(pageQuery);
        List<KubernetesDeploymentVO.Deployment> page = BeanCopierUtils.copyListProperties(table.getData(), KubernetesDeploymentVO.Deployment.class);
        return new DataTable<>(page.stream().map(e -> kubernetesDeploymentDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    public BusinessWrapper<Boolean> createKubernetesDeployment(OcKubernetesApplicationInstance ocKubernetesApplicationInstance, Integer templateId) {
        OcKubernetesTemplate ocKubernetesTemplate = ocKubernetesTemplateService.queryOcKubernetesTemplateById(templateId);
        KubernetesTemplateVO.Template deploymentTemplate = kubernetesTemplateDecorator.decorator(BeanCopierUtils.copyProperties(ocKubernetesTemplate, KubernetesTemplateVO.Template.class), ocKubernetesApplicationInstance);
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace;
        try {
            ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceByEnvType(ocKubernetesApplicationInstance.getEnvType()).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper(ErrorEnum.KUBERNETES_NAMESPACE_NOT_EXIST);
        }
        OcKubernetesCluster ocKubernetesCluster = getOcKubernetesCluster(ocKubernetesClusterNamespace);
        if (ocKubernetesCluster == null)
            return new BusinessWrapper(ErrorEnum.KUBERNETES_CLUSTER_NOT_EXIST);
        Deployment deployment = kubernetesDeploymentHandler.createOrReplaceDeployment(ocKubernetesCluster.getName(), ocKubernetesClusterNamespace.getNamespace(), deploymentTemplate.getTemplateYaml());
        if (deployment != null) {
            saveKubernetesDeployment(Maps.newHashMap(), ocKubernetesClusterNamespace, deployment);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper(ErrorEnum.KUBERNETES_CREATE_DEPLOYMENT_ERROR);
    }

    public BusinessWrapper<Boolean> deleteKubernetesDeployment(OcKubernetesApplicationInstance ocKubernetesApplicationInstance) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace;
        try {
            ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceByEnvType(ocKubernetesApplicationInstance.getEnvType()).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper(ErrorEnum.KUBERNETES_NAMESPACE_NOT_EXIST);
        }
        OcKubernetesCluster ocKubernetesCluster = getOcKubernetesCluster(ocKubernetesClusterNamespace);
        if (ocKubernetesCluster == null)
            return new BusinessWrapper(ErrorEnum.KUBERNETES_CLUSTER_NOT_EXIST);
        String deploymentName = kubernetesConfig.getDeploymentName(ocKubernetesApplicationInstance.getInstanceName());
        boolean result = kubernetesDeploymentHandler.deleteDeployment(ocKubernetesCluster.getName(), ocKubernetesClusterNamespace.getNamespace(), deploymentName);
        if (result) return BusinessWrapper.SUCCESS;
        return new BusinessWrapper(ErrorEnum.KUBERNETES_CREATE_DEPLOYMENT_ERROR);
    }

    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void syncKubernetesDeployment(int namespaceId) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceById(namespaceId);
        DeploymentList deploymentList = kubernetesDeploymentHandler.getDeploymentList(getOcKubernetesCluster(ocKubernetesClusterNamespace).getName(), ocKubernetesClusterNamespace.getNamespace());
        if (deploymentList == null || CollectionUtils.isEmpty(deploymentList.getItems())) return;
        Map<String, OcKubernetesDeployment> deploymentMap = getDeploymentMap(namespaceId);
        deploymentList.getItems().forEach(e -> saveKubernetesDeployment(deploymentMap, ocKubernetesClusterNamespace, e));
        delKubernetesDeploymentByMap(deploymentMap);
    }

    private void delKubernetesDeploymentByMap(Map<String, OcKubernetesDeployment> deploymentMap) {
        if (deploymentMap.isEmpty()) return;
        deploymentMap.keySet().forEach(k -> ocKubernetesDeploymentService.deleteOcKubernetesDeploymentById(deploymentMap.get(k).getId()));
    }

    private Map<String, OcKubernetesDeployment> getDeploymentMap(int namespaceId) {
        List<OcKubernetesDeployment> deployments = ocKubernetesDeploymentService.queryOcKubernetesDeploymentByNamespaceId(namespaceId);
        if (CollectionUtils.isEmpty(deployments)) return Maps.newHashMap();
        return deployments.stream().collect(Collectors.toMap(OcKubernetesDeployment::getName, a -> a, (k1, k2) -> k1));
    }

    private void saveKubernetesDeployment(Map<String, OcKubernetesDeployment> deploymentMap, OcKubernetesClusterNamespace ocKubernetesClusterNamespace, Deployment deployment) {
        OcKubernetesDeployment pre = KubernetesDeploymentBuilder.build(ocKubernetesClusterNamespace, deployment);
        OcKubernetesDeployment ocKubernetesDeployment = ocKubernetesDeploymentService.queryOcKubernetesDeploymentByUniqueKey(ocKubernetesClusterNamespace.getId(), pre.getName());
        if (ocKubernetesDeployment != null)
            pre = ocKubernetesDeployment;
        invokeKubernetesDeployment(pre, deployment);
        if (IDUtils.isEmpty(pre.getId())) {
            ocKubernetesDeploymentService.addOcKubernetesDeployment(pre);
        } else {
            ocKubernetesDeploymentService.updateOcKubernetesDeployment(pre);
        }
        deploymentMap.remove(pre.getName());
    }

    private void invokeKubernetesDeployment(OcKubernetesDeployment ocKubernetesDeployment, Deployment deployment) {
        if (!IDUtils.isEmpty(ocKubernetesDeployment.getInstanceId())) return;
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance = getApplicationInstanceByDeployment(deployment);
        if (ocKubernetesApplicationInstance != null) {
            ocKubernetesDeployment.setApplicationId(ocKubernetesApplicationInstance.getApplicationId());
            ocKubernetesDeployment.setInstanceId(ocKubernetesApplicationInstance.getId());
        }
    }

    private OcKubernetesApplicationInstance getApplicationInstanceByDeployment(Deployment deployment) {
        String instanceName = kubernetesConfig.getApplicationInstanceNameByDeploymentName(deployment.getMetadata().getName());
        return getApplicationInstanceByName(instanceName);
    }

    private OcKubernetesApplicationInstance getApplicationInstanceByName(String name) {
        return ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceByInstanceName(name);
    }
}
