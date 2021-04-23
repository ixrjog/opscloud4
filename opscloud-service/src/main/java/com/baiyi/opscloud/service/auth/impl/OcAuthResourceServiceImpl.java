package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthResource;
import com.baiyi.opscloud.domain.param.auth.ResourceParam;
import com.baiyi.opscloud.mapper.opscloud.OcAuthResourceMapper;
import com.baiyi.opscloud.service.auth.OcAuthResourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
    public int countByGroupId(int groupId){
        Example example = new Example(OcAuthResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId",groupId);
        return ocAuthResourceMapper.selectCountByExample(example);
    }

    @Override
    public OcAuthResource queryOcAuthResourceByName(String resourceName) {
        Example example = new Example(OcAuthResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceName", resourceName);
        return ocAuthResourceMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<OcAuthResource> queryOcAuthResourceByParam(ResourceParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAuthResource> ocAuthResourceList = ocAuthResourceMapper.queryOcAuthResourceByParam(pageQuery);
        return new DataTable<>(ocAuthResourceList, page.getTotal());
    }

    @Override
    public DataTable<OcAuthResource> queryRoleBindOcAuthResourceByParam(ResourceParam.BindResourcePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAuthResource> ocAuthResourceList = ocAuthResourceMapper.queryOcAuthRoleBindResourceByParam(pageQuery);
        return new DataTable<>(ocAuthResourceList, page.getTotal());
    }

    @Override
    public DataTable<OcAuthResource> queryRoleUnbindOcAuthResourceByParam(ResourceParam.BindResourcePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAuthResource> ocAuthResourceList = ocAuthResourceMapper.queryOcAuthRoleUnbindResourceByParam(pageQuery);
        return new DataTable<>(ocAuthResourceList, page.getTotal());
    }


    @Override
    public void addOcAuthResource(OcAuthResource ocAuthResource) {
        ocAuthResourceMapper.insert(ocAuthResource);
    }

    @Override
    public void updateOcAuthResource(OcAuthResource ocAuthResource) {
        ocAuthResourceMapper.updateByPrimaryKey(ocAuthResource);
    }

    @Override
    public void deleteOcAuthResourceById(int id) {
        ocAuthResourceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcAuthResource queryOcAuthResourceById(int id) {
        return ocAuthResourceMapper.selectByPrimaryKey(id);
    }

}
