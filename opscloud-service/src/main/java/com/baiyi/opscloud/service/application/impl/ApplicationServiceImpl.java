package com.baiyi.opscloud.service.application.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.mapper.opscloud.ApplicationMapper;
import com.baiyi.opscloud.service.application.ApplicationService;
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
 * @Date 2021/7/12 11:48 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;

    @Override
    public DataTable<Application> queryPageByParam(ApplicationParam.ApplicationPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Application.class);
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getQueryName()));
            example.setOrderByClause(String.format("replace( name, '%s', '' )", pageQuery.getQueryName()));
        } else {
            example.setOrderByClause("create_time");
        }
        List<Application> data = applicationMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<Application> queryPageByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
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
    public void add(Application application) {
        applicationMapper.insert(application);
    }

    @Override
    public void update(Application application) {
        applicationMapper.updateByPrimaryKey(application);
    }

    @Override
    public void deleteById(Integer id) {
        applicationMapper.deleteByPrimaryKey(id);
    }
}
