package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.kubernetes.*;
import com.baiyi.opscloud.domain.vo.kubernetes.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/24 4:41 下午
 * @Version 1.0
 */
public interface KubernetesFacade {

    BusinessWrapper<List<KubernetesPodVO.Pod>> queryMyKubernetesPod(KubernetesPodParam.QueryParam queryParam);

    DataTable<KubernetesClusterVO.Cluster> queryKubernetesClusterPage(KubernetesClusterParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addKubernetesCluster(KubernetesClusterVO.Cluster cluster);

    BusinessWrapper<Boolean> updateKubernetesCluster(KubernetesClusterVO.Cluster cluster);

    BusinessWrapper<Boolean> deleteKubernetesClusterById(int id);

    DataTable<KubernetesClusterNamespaceVO.Namespace> queryKubernetesClusterNamespacePage(KubernetesClusterNamespaceParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addKubernetesClusterNamespace(KubernetesClusterNamespaceVO.Namespace namespace);

    BusinessWrapper<Boolean> updateKubernetesClusterNamespace(KubernetesClusterNamespaceVO.Namespace namespace);

    BusinessWrapper<Boolean> deleteKubernetesClusterNamespaceById(int id);

    BusinessWrapper<List<KubernetesClusterNamespaceVO.Namespace>> queryKubernetesExcludeNamespace(KubernetesClusterNamespaceParam.ExcludeQuery excludeQuery);

    void syncKubernetesDeployment(int namespaceId);

    void syncKubernetesService(int namespaceId);

    DataTable<KubernetesDeploymentVO.Deployment> queryKubernetesDeploymentPage(KubernetesDeploymentParam.PageQuery pageQuery);

    DataTable<KubernetesTemplateVO.Template> queryKubernetesTemplatePage(KubernetesTemplateParam.PageQuery pageQuery);

    /**
     * 查询实例模版
     *
     * @param pageQuery
     * @return
     */
    DataTable<KubernetesTemplateVO.Template> queryKubernetesTemplatePage(KubernetesApplicationInstanceParam.TemplatePageQuery pageQuery);

    BusinessWrapper<Boolean> addKubernetesTemplate(KubernetesTemplateVO.Template template);

    BusinessWrapper<Boolean> updateKubernetesTemplate(KubernetesTemplateVO.Template template);

    BusinessWrapper<Boolean> deleteKubernetesTemplateById(int id);

    DataTable<KubernetesServiceVO.Service> queryKubernetesServicePage(KubernetesServiceParam.PageQuery pageQuery);

    BusinessWrapper<KubernetesServiceVO.Service> queryKubernetesServiceByParam(KubernetesServiceParam.QueryParam queryParam);

    BusinessWrapper<Boolean> deleteKubernetesServiceById(int id);
}
