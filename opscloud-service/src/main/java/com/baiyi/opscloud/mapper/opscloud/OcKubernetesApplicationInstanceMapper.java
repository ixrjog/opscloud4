package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesApplicationInstance;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesApplicationInstanceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesApplicationInstanceMapper extends Mapper<OcKubernetesApplicationInstance> {

    List<OcKubernetesApplicationInstance> queryOcKubernetesApplicationInstanceByParam(KubernetesApplicationInstanceParam.PageQuery pageQuery);
}