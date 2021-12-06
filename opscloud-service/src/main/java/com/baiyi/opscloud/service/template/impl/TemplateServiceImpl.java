package com.baiyi.opscloud.service.template.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Template;
import com.baiyi.opscloud.mapper.opscloud.TemplateMapper;
import com.baiyi.opscloud.service.template.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/3 4:27 PM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;

    @Override
    public Template getById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Template> listByTemplate(Template template) {
        return templateMapper.select(template);
    }

}
