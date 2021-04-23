package com.baiyi.opscloud.service.file.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcFileTemplate;
import com.baiyi.opscloud.mapper.opscloud.OcFileTemplateMapper;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/10/19 1:25 下午
 * @Version 1.0
 */
@Service
public class OcFileTemplateServiceImpl implements OcFileTemplateService {

    @Resource
    private OcFileTemplateMapper ocFileTemplateMapper;

    @Override
    public OcFileTemplate queryOcFileTemplateByUniqueKey(String templateKey, int envType) {
        Example example = new Example(OcFileTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateKey", templateKey);
        criteria.andEqualTo("envType", envType);
        return ocFileTemplateMapper.selectOneByExample(example);
    }

    @Override
    public void updateOcFileTemplate(OcFileTemplate ocFileTemplate) {
        ocFileTemplateMapper.updateByPrimaryKey(ocFileTemplate);
    }
}
