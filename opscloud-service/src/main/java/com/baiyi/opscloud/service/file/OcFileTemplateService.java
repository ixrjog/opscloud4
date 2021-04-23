package com.baiyi.opscloud.service.file;

import com.baiyi.opscloud.domain.generator.opscloud.OcFileTemplate;

/**
 * @Author baiyi
 * @Date 2020/10/19 1:24 下午
 * @Version 1.0
 */
public interface OcFileTemplateService {

    OcFileTemplate queryOcFileTemplateByUniqueKey(String templateKey, int envType);

    void updateOcFileTemplate(OcFileTemplate ocFileTemplate);
}
