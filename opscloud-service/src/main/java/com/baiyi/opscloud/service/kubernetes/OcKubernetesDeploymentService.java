package com.baiyi.opscloud.service.kubernetes;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesDeployment;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesDeploymentParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/29 2:33 下午
 * @Version 1.0
 */
public interface OcKubernetesDeploymentService {

    DataTable<OcKubernetesDeployment> queryOcKubernetesDeploymentByParam(KubernetesDeploymentParam.PageQuery pageQuery);

    OcKubernetesDeployment queryOcKubernetesDeploymentByUniqueKey(int namespaceId, String name);

    OcKubernetesDeployment queryOcKubernetesDeploymentByInstanceId(int instanceId);

    OcKubernetesDeployment queryOcKubernetesDeploymentById(int id);

    void addOcKubernetesDeployment(OcKubernetesDeployment ocKubernetesDeployment);

    void updateOcKubernetesDeployment(OcKubernetesDeployment ocKubernetesDeployment);

    void deleteOcKubernetesDeploymentById(int id);

    List<OcKubernetesDeployment> queryOcKubernetesDeploymentByNamespaceId(int namespaceId);
}
