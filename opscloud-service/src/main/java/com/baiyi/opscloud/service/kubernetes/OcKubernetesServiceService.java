package com.baiyi.opscloud.service.kubernetes;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesService;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesServiceParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 9:46 上午
 * @Version 1.0
 */
public interface OcKubernetesServiceService {

    DataTable<OcKubernetesService> queryOcKubernetesServiceByParam(KubernetesServiceParam.PageQuery pageQuery);

    List<OcKubernetesService> queryOcKubernetesServiceByNamespaceId(int namespaceId);

    OcKubernetesService queryOcKubernetesServiceByUniqueKey(int namespaceId, String name);

    OcKubernetesService queryOcKubernetesServiceByInstanceId(int instanceId);

    OcKubernetesService queryOcKubernetesServiceById(int id);

    void addOcKubernetesService(OcKubernetesService ocKubernetesService);

    void updateOcKubernetesService(OcKubernetesService ocKubernetesService);

    void deleteOcKubernetesServiceById(int id);
}
