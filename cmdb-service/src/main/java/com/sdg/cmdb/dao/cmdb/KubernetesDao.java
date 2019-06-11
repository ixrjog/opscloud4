package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.kubernetes.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface KubernetesDao {

    int addCluster(KubernetesClusterDO kubernetesClusterDO);

    int delCluster(@Param("id") long id);

    int updateCluster(KubernetesClusterDO kubernetesClusterDO);

    List<KubernetesClusterDO> queryCluster();

    KubernetesClusterDO getClusterByName(@Param("name") String name);

    KubernetesClusterDO getCluster(@Param("id") long id);

    KubernetesClusterDO queryClusterByEnv(@Param("env") int env);

    int addNamespace(KubernetesNamespaceDO kubernetesNamespaceDO);

    int delNamespace(@Param("id") long id);

    int updateNamespace(KubernetesNamespaceDO kubernetesNamespaceDO);

    List<KubernetesNamespaceDO> queryNamespaceByClusterId(@Param("clusterId") long clusterId);

    List<KubernetesNamespaceDO> queryNamespace();

    KubernetesNamespaceDO getNamespace(KubernetesNamespaceDO kubernetesNamespaceDO);

    KubernetesNamespaceDO getNamespaceById(@Param("id") long id);

    int addService(KubernetesServiceDO kubernetesServiceDO);

    int delService(@Param("id") long id);

    int updateService(KubernetesServiceDO kubernetesServiceDO);

    KubernetesServiceDO getServiceByAppName(@Param("appName") String appName);

    KubernetesServiceVO queryServiceByPort(@Param("namespaceId") long namespaceId,
                                           @Param("serverGroupId") long serverGroupId,
                                           @Param("portName") String portName);

    //List<KubernetesServiceDO> queryServiceByClusterId(@Param("clusterId") long clusterId);
    KubernetesServiceDO getService(KubernetesServiceDO kubernetesServiceDO);
    KubernetesServiceDO getServiceById(@Param("id") long id);

    int getServiceSize(@Param("namespaceId") long namespaceId,
                       @Param("name") String name, @Param("portName") String portName);

    List<KubernetesServiceDO> queryServicePage(@Param("namespaceId") long namespaceId,
                                               @Param("name") String name,
                                               @Param("portName") String portName,
                                               @Param("pageStart") int pageStart,
                                               @Param("pageLength") int pageLength);

    int addPort(KubernetesServicePortDO kubernetesServicePortDO);

    int delPort(@Param("id") long id);

    int updatePort(KubernetesServicePortDO kubernetesServicePortDO);

    List<KubernetesServicePortDO> queryServicePortByServiceId(@Param("serviceId") long serviceId);
}
