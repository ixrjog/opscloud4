package com.baiyi.opscloud.service.template;

import com.baiyi.opscloud.domain.generator.opscloud.Template;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/3 4:27 PM
 * @Version 1.0
 */
public interface TemplateService {

    Template getById(Integer id);

    List<Template> listByTemplate(Template template);

}
