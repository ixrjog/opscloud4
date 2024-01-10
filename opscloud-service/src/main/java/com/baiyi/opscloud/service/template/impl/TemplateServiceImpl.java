package com.baiyi.opscloud.service.template.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Template;
import com.baiyi.opscloud.domain.param.template.TemplateParam;
import com.baiyi.opscloud.mapper.TemplateMapper;
import com.baiyi.opscloud.service.template.TemplateService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/3 4:27 PM
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;

    @Override
    public DataTable<Template> queryPageByParam(TemplateParam.TemplatePageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        if (IdUtil.isNotEmpty(pageQuery.getEnvType())) {
            criteria.andEqualTo("envType", pageQuery.getEnvType());
        }
        if (StringUtils.isNotBlank(pageQuery.getInstanceType())) {
            criteria.andLike("instanceType", SQLUtil.toLike(pageQuery.getInstanceType()));
        }
        if (StringUtils.isNotBlank(pageQuery.getTemplateKey())) {
            criteria.andLike("templateKey", SQLUtil.toLike(pageQuery.getTemplateKey()));
        }
        if (StringUtils.isNotBlank(pageQuery.getTemplateType())) {
            criteria.andLike("templateType", SQLUtil.toLike(pageQuery.getTemplateType()));
        }
        if (StringUtils.isNotBlank(pageQuery.getKind())) {
            criteria.andEqualTo("kind", pageQuery.getKind());
        }
        List<Template> data = templateMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Template getById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Template> listByTemplate(Template template) {
        return templateMapper.select(template);
    }

    @Override
    public void add(Template template) {
        templateMapper.insert(template);
    }

    @Override
    public void updateByPrimaryKeySelective(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public void deleteById(Integer id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<String> getKindOptions() {
        return templateMapper.getKindOptions();
    }

}