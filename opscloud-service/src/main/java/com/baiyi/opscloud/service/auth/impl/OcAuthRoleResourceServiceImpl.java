package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAuthRoleResource;
import com.baiyi.opscloud.mapper.opscloud.OcAuthRoleResourceMapper;
import com.baiyi.opscloud.service.auth.OcAuthRoleResourceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:13 下午
 * @Version 1.0
 */
@Service
public class OcAuthRoleResourceServiceImpl implements OcAuthRoleResourceService {

    @Resource
    private OcAuthRoleResourceMapper ocAuthRoleResourceMapper;

    @Override
    public int countByResourceId(int resourceId){
        Example example = new Example(OcAuthRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceId",resourceId);
        return ocAuthRoleResourceMapper.selectCountByExample(example);
    }

    @Override
    public void addOcAuthRoleResource(OcAuthRoleResource ocAuthRoleResource){
        ocAuthRoleResourceMapper.insert(ocAuthRoleResource);
    }

    @Override
    public void delOcAuthRoleResourceById(int id){
        ocAuthRoleResourceMapper.deleteByPrimaryKey(id);
    }

}
