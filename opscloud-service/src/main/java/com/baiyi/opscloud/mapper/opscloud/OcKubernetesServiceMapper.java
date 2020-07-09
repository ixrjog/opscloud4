package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesService;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesServiceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesServiceMapper extends Mapper<OcKubernetesService> {

    List<OcKubernetesService> queryOcKubernetesServiceByParam(KubernetesServiceParam.PageQuery pageQuery);
}