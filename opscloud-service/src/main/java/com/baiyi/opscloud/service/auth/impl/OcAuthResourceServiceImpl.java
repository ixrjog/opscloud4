package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.OcAuthResource;
import com.baiyi.opscloud.mapper.OcAuthResourceMapper;
import com.baiyi.opscloud.service.auth.OcAuthResourceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:06 下午
 * @Version 1.0
 */
@Service
public class OcAuthResourceServiceImpl implements OcAuthResourceService {

    @Resource
    private OcAuthResourceMapper ocAuthResourceMapper;

    @Override
    public OcAuthResource queryOcAuthResourceByName(String resourceName) {
        Example example = new Example(OcAuthResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceName",resourceName);
        return ocAuthResourceMapper.selectOneByExample(example);
    }
}
