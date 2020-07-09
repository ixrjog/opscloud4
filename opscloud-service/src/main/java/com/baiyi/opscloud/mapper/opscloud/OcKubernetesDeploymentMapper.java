package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesDeployment;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesDeploymentParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesDeploymentMapper extends Mapper<OcKubernetesDeployment> {

    List<OcKubernetesDeployment> queryOcKubernetesDeploymentByParam(KubernetesDeploymentParam.PageQuery pageQuery);

}