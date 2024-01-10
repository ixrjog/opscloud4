package com.baiyi.opscloud.service.template;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Template;
import com.baiyi.opscloud.domain.param.template.TemplateParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/3 4:27 PM
 * @Version 1.0
 */
public interface TemplateService {

    DataTable<Template> queryPageByParam(TemplateParam.TemplatePageQuery pageQuery);

    Template getById(Integer id);

    List<Template> listByTemplate(Template template);

    void add(Template template);

    void updateByPrimaryKeySelective(Template template);

    void deleteById(Integer id);

    List<String> getKindOptions();

}