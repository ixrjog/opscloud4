package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoJobRequestParam;
import com.baiyi.opscloud.mapper.LeoJobMapper;
import com.baiyi.opscloud.service.leo.LeoJobService;
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
 * @Date 2022/11/4 14:38
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class LeoJobServiceImpl implements LeoJobService {

    private final LeoJobMapper leoJobMapper;

    @Override
    public DataTable<LeoJob> queryJobPage(LeoJobParam.JobPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<LeoJob> data = leoJobMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<LeoJob> queryJobPage(SubscribeLeoJobRequestParam pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(LeoJob.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        criteria.andEqualTo("applicationId", pageQuery.getApplicationId())
                .andEqualTo("envType", pageQuery.getEnvType())
                .andEqualTo("isActive", true);
        List<LeoJob> data = leoJobMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<LeoJob> queryJob(Integer applicationId, Integer envType) {
        Example example = new Example(LeoJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId)
                .andEqualTo("envType", envType)
                .andEqualTo("isActive", true);
        return leoJobMapper.selectByExample(example);
    }

    @Override
    public List<LeoJob> queryJobWithSubscribe(Integer applicationId, Integer envType, String buildType) {
        Example example = new Example(LeoJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId)
                .andEqualTo("envType", envType)
                .andEqualTo("isActive", true)
                .andEqualTo("buildType", buildType);
        return leoJobMapper.selectByExample(example);
    }

    @Override
    public List<LeoJob> queryJobWithApplicationId(Integer applicationId) {
        Example example = new Example(LeoJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        return leoJobMapper.selectByExample(example);
    }

    @Override
    public List<LeoJob> queryAutoBuildJob(Integer applicationId, String branch) {
        Example example = new Example(LeoJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId)
                .andEqualTo("branch", branch);
        return leoJobMapper.selectByExample(example);
    }

    @Override
    public void add(LeoJob leoJob) {
        leoJobMapper.insert(leoJob);
    }

    @Override
    public void update(LeoJob leoJob) {
        leoJobMapper.updateByPrimaryKey(leoJob);
    }

    @Override
    public LeoJob getById(Integer id) {
        return leoJobMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteById(Integer id) {
        leoJobMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(LeoJob leoJob) {
        leoJobMapper.updateByPrimaryKeySelective(leoJob);
    }

    @Override
    public int countWithTemplateId(Integer templateId) {
        Example example = new Example(LeoJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", templateId);
        return leoJobMapper.selectCountByExample(example);
    }

    @Override
    public int countWithReport() {
        Example example = new Example(LeoJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true);
        return leoJobMapper.selectCountByExample(example);
    }

    @Override
    public List<LeoJob> queryAll() {
        return leoJobMapper.selectAll();
    }

    @Override
    public List<LeoJob> queryUpgradeableJobs(Integer templateId, String templateVersion) {
        Example example = new Example(LeoJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", templateId)
                .andNotEqualTo("templateVersion", templateVersion);
        return leoJobMapper.selectByExample(example);
    }

}