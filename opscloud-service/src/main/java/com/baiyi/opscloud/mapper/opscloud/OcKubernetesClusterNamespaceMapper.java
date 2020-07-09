package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesClusterNamespace;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesClusterNamespaceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesClusterNamespaceMapper extends Mapper<OcKubernetesClusterNamespace> {

    List<OcKubernetesClusterNamespace> queryOcKubernetesClusterNamespaceByParam(KubernetesClusterNamespaceParam.PageQuery pageQuery);
}