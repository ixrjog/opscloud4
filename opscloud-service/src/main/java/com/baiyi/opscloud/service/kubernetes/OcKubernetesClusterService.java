package com.baiyi.opscloud.service.kubernetes;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesClusterParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/24 4:22 下午
 * @Version 1.0
 */
public interface OcKubernetesClusterService {

    List<OcKubernetesCluster> queryAll();

    OcKubernetesCluster queryOcKubernetesClusterById(int id);

    OcKubernetesCluster queryOcKubernetesClusterByName(String name);

    DataTable<OcKubernetesCluster> queryOcKubernetesClusterByParam(KubernetesClusterParam.PageQuery pageQuery);

    void addOcKubernetesCluster(OcKubernetesCluster ocKubernetesCluster);

    void updateOcKubernetesCluster(OcKubernetesCluster ocKubernetesCluster);

    void deleteOcKubernetesClusterById(int id);
}
