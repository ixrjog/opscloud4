package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesApplication;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesApplicationParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesApplicationMapper extends Mapper<OcKubernetesApplication> {

    List<OcKubernetesApplication> queryOcKubernetesApplicationByParam(KubernetesApplicationParam.PageQuery pageQuery);
}