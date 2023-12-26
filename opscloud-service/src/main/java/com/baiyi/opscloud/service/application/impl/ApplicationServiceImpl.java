package com.baiyi.opscloud.service.application.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.mapper.ApplicationMapper;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/12 11:48 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;

    @Override
    public DataTable<Application> queryPageByParam(ApplicationParam.ApplicationPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Application> data = applicationMapper.queryApplicationByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<Application> queryAll() {
        return applicationMapper.selectAll();
    }

    @Override
    public DataTable<Application> queryPageByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Application> data = applicationMapper.queryUserPermissionApplicationByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Application getById(Integer id) {
        return applicationMapper.selectByPrimaryKey(id);
    }

    @Override
    public Application getByKey(String applicationKey) {
        Example example = new Example(Application.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationKey", applicationKey);
        return applicationMapper.selectOneByExample(example);
    }

    @Override
    public Application getByName(String name) {
        Example example = new Example(Application.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return applicationMapper.selectOneByExample(example);
    }

    @Override
    public void add(Application application) {
        applicationMapper.insert(application);
    }

    @Override
    public void update(Application application) {
        applicationMapper.updateByPrimaryKey(application);
    }

    @Override
    public void updateByPrimaryKeySelective(Application application) {
        applicationMapper.updateByPrimaryKeySelective(application);
    }

    @Override
    public void deleteById(Integer id) {
        applicationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int countWithReport() {
        Example example = new Example(Application.class);
        return applicationMapper.selectCountByExample(example);
    }

}