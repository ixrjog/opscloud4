package com.baiyi.opscloud.service.kubernetes;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesTemplate;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesTemplateParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/30 10:57 上午
 * @Version 1.0
 */
public interface OcKubernetesTemplateService {

    DataTable<OcKubernetesTemplate> queryOcKubernetesTemplateByParam(KubernetesTemplateParam.PageQuery pageQuery);

    List<OcKubernetesTemplate> queryOcKubernetesTemplateByType(String templateType);

    OcKubernetesTemplate queryOcKubernetesTemplateById(Integer id);

    void addOcKubernetesTemplate(OcKubernetesTemplate ocKubernetesTemplate);

    void updateOcKubernetesTemplate(OcKubernetesTemplate ocKubernetesTemplate);

    void deleteOcKubernetesTemplateById(int id);
}
