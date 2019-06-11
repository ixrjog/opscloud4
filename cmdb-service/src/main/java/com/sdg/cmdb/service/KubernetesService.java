package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.kubernetes.*;

import java.util.List;

public interface KubernetesService {

    BusinessWrapper<Boolean> sync();

    List<KubernetesClusterVO> queryClusterPage();

    BusinessWrapper<Boolean> saveCluster(KubernetesClusterVO kubernetesClusterVO);

    List<KubernetesNamespaceVO> queryNamespacePage();

    TableVO<List<KubernetesServiceVO>> queryServicePage(long namespaceId, String name, String portName, int page, int length);

    BusinessWrapper<Boolean> saveService(KubernetesServiceVO kubernetesServiceVO);

    BusinessWrapper<Boolean> delService(long id);

    KubernetesServiceDO getKubernetesService(long serverGroupId, int env, String portName);

    KubernetesServiceCluster getServerList(long serverGroupId, int env, String portName, int size);

    KubernetesServiceVO getService(String dubbo, String cluster, String namespace);

    // 生成tcpDubbo配置规则
    void syncDubbo();

    /**
     * 同步标签
     * @param clusterName
     * @return
     */
    BusinessWrapper<Boolean> syncClusterLabel(String clusterName);

}
