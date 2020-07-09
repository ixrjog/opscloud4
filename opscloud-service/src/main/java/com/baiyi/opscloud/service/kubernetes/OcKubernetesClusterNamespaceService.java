package com.baiyi.opscloud.service.kubernetes;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesClusterNamespace;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesClusterNamespaceParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/28 1:54 下午
 * @Version 1.0
 */
public interface OcKubernetesClusterNamespaceService {

    DataTable<OcKubernetesClusterNamespace> queryOcKubernetesClusterNamespaceByParam(KubernetesClusterNamespaceParam.PageQuery pageQuery);

    void addOcKubernetesClusterNamespace(OcKubernetesClusterNamespace ocKubernetesClusterNamespace);

    void updateOcKubernetesClusterNamespace(OcKubernetesClusterNamespace ocKubernetesClusterNamespace);

    void deleteOcKubernetesClusterNamespaceById(int id);

    List<OcKubernetesClusterNamespace> queryOcKubernetesClusterNamespaceByClusterId(int clusterId);

    OcKubernetesClusterNamespace queryOcKubernetesClusterNamespaceById(int id);

    List<OcKubernetesClusterNamespace> queryOcKubernetesClusterNamespaceByEnvType(int envType);

    OcKubernetesClusterNamespace queryOcKubernetesClusterNamespaceByUniqueKey(int envType, String namespace);
}
