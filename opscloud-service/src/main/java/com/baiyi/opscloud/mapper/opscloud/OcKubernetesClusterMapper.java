package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesClusterParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesClusterMapper extends Mapper<OcKubernetesCluster> {

    List<OcKubernetesCluster> queryOcKubernetesClusterByParam(KubernetesClusterParam.PageQuery pageQuery);
}