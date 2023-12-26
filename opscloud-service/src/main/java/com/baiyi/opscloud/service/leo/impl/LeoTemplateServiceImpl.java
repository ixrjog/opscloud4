package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;
import com.baiyi.opscloud.mapper.LeoTemplateMapper;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/1 14:17
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoTemplateServiceImpl implements LeoTemplateService {

    private final LeoTemplateMapper templateMapper;

    @Override
    public DataTable<LeoTemplate> queryTemplatePage(LeoTemplateParam.TemplatePageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<LeoTemplate> data = templateMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void add(LeoTemplate leoTemplate) {
        templateMapper.insert(leoTemplate);
    }

    @Override
    public void update(LeoTemplate leoTemplate) {
        templateMapper.updateByPrimaryKey(leoTemplate);
    }

    @Override
    public void updateByPrimaryKeySelective(LeoTemplate leoTemplate) {
        templateMapper.updateByPrimaryKeySelective(leoTemplate);
    }

    @Override
    public LeoTemplate getById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteById(Integer id) {
        templateMapper.deleteByPrimaryKey(id);
    }

}