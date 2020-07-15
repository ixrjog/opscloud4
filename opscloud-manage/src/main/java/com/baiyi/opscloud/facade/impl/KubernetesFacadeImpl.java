package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.kubernetes.KubernetesClusterDecorator;
import com.baiyi.opscloud.decorator.kubernetes.KubernetesClusterNamespaceDecorator;
import com.baiyi.opscloud.decorator.kubernetes.KubernetesTemplateDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.kubernetes.*;
import com.baiyi.opscloud.domain.vo.kubernetes.*;
import com.baiyi.opscloud.facade.KubernetesFacade;
import com.baiyi.opscloud.facade.kubernetes.KubernetesDeploymentFacade;
import com.baiyi.opscloud.facade.kubernetes.KubernetesPodFacade;
import com.baiyi.opscloud.facade.kubernetes.KubernetesServiceFacade;
import com.baiyi.opscloud.kubernetes.client.KubernetesClientContainer;
import com.baiyi.opscloud.kubernetes.confg.KubernetesConfig;
import com.baiyi.opscloud.kubernetes.handler.KubernetesClusterHandler;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterNamespaceService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesTemplateService;
import com.baiyi.opscloud.util.KubeconfigUtils;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/24 4:41 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class KubernetesFacadeImpl implements KubernetesFacade {

    @Resource
    private OcKubernetesClusterService ocKubernetesClusterService;

    @Resource
    private KubernetesClusterDecorator kubernetesClusterDecorator;

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    @Resource
    private KubernetesClusterNamespaceDecorator kubernetesClusterNamespaceDecorator;

    @Resource
    private KubernetesClusterHandler kubernetesClusterHandler;

    @Resource
    private KubernetesClientContainer kubernetesClientContainer;

    @Resource
    private OcKubernetesTemplateService ocKubernetesTemplateService;

    @Resource
    private KubernetesServiceFacade kubernetesServiceFacade;

    @Resource
    private KubernetesDeploymentFacade kubernetesDeploymentFacade;

    @Resource
    private KubernetesTemplateDecorator kubernetesTemplateDecorator;

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private KubernetesConfig kubernetesConfig;

    @Resource
    private KubernetesPodFacade kubernetesPodFacade;

    @Override
    public BusinessWrapper<List<KubernetesPodVO.Pod>> queryMyKubernetesPod(KubernetesPodParam.QueryParam queryParam) {
        return kubernetesPodFacade.queryMyKubernetesPod(queryParam);
    }

    @Override
    public DataTable<KubernetesClusterVO.Cluster> queryKubernetesClusterPage(KubernetesClusterParam.PageQuery pageQuery) {
        DataTable<OcKubernetesCluster> table = ocKubernetesClusterService.queryOcKubernetesClusterByParam(pageQuery);
        List<KubernetesClusterVO.Cluster> page = BeanCopierUtils.copyListProperties(table.getData(), KubernetesClusterVO.Cluster.class);
        return new DataTable<>(page.stream().map(e -> kubernetesClusterDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addKubernetesCluster(KubernetesClusterVO.Cluster cluster) {
        OcKubernetesCluster ocKubernetesCluster = BeanCopierUtils.copyProperties(cluster, OcKubernetesCluster.class);
        ocKubernetesClusterService.addOcKubernetesCluster(ocKubernetesCluster);
        KubeconfigUtils.writeKubeconfig(ocKubernetesCluster);
        kubernetesClientContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateKubernetesCluster(KubernetesClusterVO.Cluster cluster) {
        OcKubernetesCluster ocKubernetesCluster = BeanCopierUtils.copyProperties(cluster, OcKubernetesCluster.class);
        ocKubernetesClusterService.updateOcKubernetesCluster(ocKubernetesCluster);
        KubeconfigUtils.writeKubeconfig(ocKubernetesCluster);
        kubernetesClientContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteKubernetesClusterById(int id) {
        // TODO
        OcKubernetesCluster ocKubernetesCluster = ocKubernetesClusterService.queryOcKubernetesClusterById(id);
        KubeconfigUtils.deleteKubeconfig(ocKubernetesCluster);
        kubernetesClientContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<KubernetesClusterNamespaceVO.Namespace> queryKubernetesClusterNamespacePage(KubernetesClusterNamespaceParam.PageQuery pageQuery) {
        DataTable<OcKubernetesClusterNamespace> table = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceByParam(pageQuery);
        List<KubernetesClusterNamespaceVO.Namespace> page = BeanCopierUtils.copyListProperties(table.getData(), KubernetesClusterNamespaceVO.Namespace.class);
        return new DataTable<>(page.stream().map(e -> kubernetesClusterNamespaceDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addKubernetesClusterNamespace(KubernetesClusterNamespaceVO.Namespace namespace) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace = BeanCopierUtils.copyProperties(namespace, OcKubernetesClusterNamespace.class);
        try {
            ocKubernetesClusterNamespaceService.addOcKubernetesClusterNamespace(ocKubernetesClusterNamespace);
        } catch (Exception e) {
            return new BusinessWrapper<>(10000, "新增命名空间错误");
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateKubernetesClusterNamespace(KubernetesClusterNamespaceVO.Namespace namespace) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace = BeanCopierUtils.copyProperties(namespace, OcKubernetesClusterNamespace.class);
        try {
            ocKubernetesClusterNamespaceService.updateOcKubernetesClusterNamespace(ocKubernetesClusterNamespace);
        } catch (Exception e) {
            return new BusinessWrapper<>(10000, "更新命名空间错误");
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteKubernetesClusterNamespaceById(int id) {
        // TODO
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<List<KubernetesClusterNamespaceVO.Namespace>> queryKubernetesExcludeNamespace(KubernetesClusterNamespaceParam.ExcludeQuery excludeQuery) {
        OcKubernetesCluster ocKubernetesCluster = ocKubernetesClusterService.queryOcKubernetesClusterById(excludeQuery.getClusterId());
        NamespaceList namespaceList = kubernetesClusterHandler.getNamespaceList(ocKubernetesCluster.getName());
        List<Namespace> list = namespaceList.getItems().stream().filter(e ->
                kubernetesConfig.namespaceFilter(e.getMetadata().getName())
        ).collect(Collectors.toList());
        List<KubernetesClusterNamespaceVO.Namespace> namespaces = list.stream().map(e -> {
            KubernetesClusterNamespaceVO.Namespace namespace = new KubernetesClusterNamespaceVO.Namespace();
            namespace.setClusterId(excludeQuery.getClusterId());
            namespace.setNamespace(e.getMetadata().getName());
            return namespace;
        }).collect(Collectors.toList());
        return new BusinessWrapper(namespaces);
    }

    @Override
    public void syncKubernetesDeployment(int namespaceId) {
        kubernetesDeploymentFacade.syncKubernetesDeployment(namespaceId);
    }

    @Override
    public void syncKubernetesService(int namespaceId) {
        kubernetesServiceFacade.syncKubernetesService(namespaceId);
    }

    @Override
    public DataTable<KubernetesDeploymentVO.Deployment> queryKubernetesDeploymentPage(KubernetesDeploymentParam.PageQuery pageQuery) {
        return kubernetesDeploymentFacade.queryKubernetesDeploymentPage(pageQuery);
    }

    @Override
    public DataTable<KubernetesTemplateVO.Template> queryKubernetesTemplatePage(KubernetesTemplateParam.PageQuery pageQuery) {
        DataTable<OcKubernetesTemplate> table = getKubernetesTemplateDataTable(pageQuery);
        List<KubernetesTemplateVO.Template> page = BeanCopierUtils.copyListProperties(table.getData(), KubernetesTemplateVO.Template.class);
        return new DataTable<>(page.stream().map(e -> kubernetesTemplateDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<KubernetesTemplateVO.Template> queryKubernetesTemplatePage(KubernetesApplicationInstanceParam.TemplatePageQuery pageQuery) {
        DataTable<OcKubernetesTemplate> table = getKubernetesTemplateDataTable(pageQuery);
        List<KubernetesTemplateVO.Template> page = BeanCopierUtils.copyListProperties(table.getData(), KubernetesTemplateVO.Template.class);
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(pageQuery.getInstanceId());
        return new DataTable<>(page.stream().map(e -> kubernetesTemplateDecorator.decorator(e, ocKubernetesApplicationInstance)).collect(Collectors.toList()), table.getTotalNum());
    }

    private DataTable<OcKubernetesTemplate> getKubernetesTemplateDataTable(KubernetesTemplateParam.PageQuery pageQuery) {
        return ocKubernetesTemplateService.queryOcKubernetesTemplateByParam(pageQuery);
    }

    @Override
    public BusinessWrapper<Boolean> addKubernetesTemplate(KubernetesTemplateVO.Template template) {
        OcKubernetesTemplate ocKubernetesTemplate = BeanCopierUtils.copyProperties(template, OcKubernetesTemplate.class);
        ocKubernetesTemplateService.addOcKubernetesTemplate(ocKubernetesTemplate);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateKubernetesTemplate(KubernetesTemplateVO.Template template) {
        OcKubernetesTemplate ocKubernetesTemplate = BeanCopierUtils.copyProperties(template, OcKubernetesTemplate.class);
        ocKubernetesTemplateService.updateOcKubernetesTemplate(ocKubernetesTemplate);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteKubernetesTemplateById(int id) {
        ocKubernetesTemplateService.deleteOcKubernetesTemplateById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<KubernetesServiceVO.Service> queryKubernetesServicePage(KubernetesServiceParam.PageQuery pageQuery) {
        return kubernetesServiceFacade.queryKubernetesServicePage(pageQuery);
    }

    @Override
    public BusinessWrapper<KubernetesServiceVO.Service> queryKubernetesServiceByParam(KubernetesServiceParam.QueryParam queryParam) {
        return kubernetesServiceFacade.queryKubernetesServiceByParam(queryParam);
    }

    @Override
    public BusinessWrapper<Boolean> deleteKubernetesServiceById(int id){
        return kubernetesServiceFacade.deleteKubernetesServiceById(id);
    }


}
