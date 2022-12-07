package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.param.leo.request.QueryLeoDeployRequestParam;
import com.baiyi.opscloud.mapper.opscloud.LeoDeployMapper;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/5 18:02
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoDeployServiceImpl implements LeoDeployService {

    private final LeoDeployMapper leoDeployMapper;

    @Override
    public int getMaxDeployNumberWithJobId(Integer jobId) {
        Integer maxDeployNumber = leoDeployMapper.getMaxDeployNumberWithJobId(jobId);
        return maxDeployNumber == null ? 0 : maxDeployNumber;
    }

    @Override
    public void add(LeoDeploy leoDeploy) {
        leoDeployMapper.insert(leoDeploy);
    }

    @Override
    public LeoDeploy getById(Integer id) {
        return leoDeployMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(LeoDeploy leoDeploy) {
        leoDeployMapper.updateByPrimaryKeySelective(leoDeploy);
    }

    @Override
    public DataTable<LeoDeploy> queryDeployPage(QueryLeoDeployRequestParam pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(LeoDeploy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("jobId", pageQuery.getJobIds());
        example.setOrderByClause("id desc");
        List<LeoDeploy> data = leoDeployMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}
