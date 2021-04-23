package com.baiyi.opscloud.service.kubernetes.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesTemplate;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesTemplateParam;
import com.baiyi.opscloud.mapper.opscloud.OcKubernetesTemplateMapper;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/30 10:58 上午
 * @Version 1.0
 */
@Service
public class OcKubernetesTemplateServiceImpl implements OcKubernetesTemplateService {

    @Resource
    private OcKubernetesTemplateMapper ocKubernetesTemplateMapper;

    @Override
    public DataTable<OcKubernetesTemplate> queryOcKubernetesTemplateByParam(KubernetesTemplateParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcKubernetesTemplate> list = ocKubernetesTemplateMapper.queryKubernetesTemplateByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcKubernetesTemplate> queryOcKubernetesTemplateByType(String templateType) {
        Example example = new Example(OcKubernetesTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateType", templateType);
        return ocKubernetesTemplateMapper.selectByExample(example);
    }

    @Override
    public OcKubernetesTemplate queryOcKubernetesTemplateById(Integer id) {
        return ocKubernetesTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcKubernetesTemplate(OcKubernetesTemplate ocKubernetesTemplate) {
        ocKubernetesTemplateMapper.insert(ocKubernetesTemplate);
    }

    @Override
    public void updateOcKubernetesTemplate(OcKubernetesTemplate ocKubernetesTemplate) {
        ocKubernetesTemplateMapper.updateByPrimaryKey(ocKubernetesTemplate);
    }

    @Override
    public void deleteOcKubernetesTemplateById(int id) {
        ocKubernetesTemplateMapper.deleteByPrimaryKey(id);
    }

}
