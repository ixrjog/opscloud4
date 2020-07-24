package com.baiyi.opscloud.service.kubernetes;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesApplication;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesApplicationParam;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:13 下午
 * @Version 1.0
 */
public interface OcKubernetesApplicationService {

    DataTable<OcKubernetesApplication> queryOcKubernetesApplicationByParam(KubernetesApplicationParam.PageQuery pageQuery);

    void addOcKubernetesApplication(OcKubernetesApplication ocKubernetesApplication);

    void updateOcKubernetesApplication(OcKubernetesApplication ocKubernetesApplication);

    void deleteOcKubernetesApplicationById(int id);

   OcKubernetesApplication queryOcKubernetesApplicationById(int id);

    OcKubernetesApplication queryOcKubernetesApplicationByName(String name);
}
