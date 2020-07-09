package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesTemplate;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesTemplateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesTemplateMapper extends Mapper<OcKubernetesTemplate> {

    List<OcKubernetesTemplate> queryKubernetesTemplateByParam(KubernetesTemplateParam.PageQuery pageQuery);
}