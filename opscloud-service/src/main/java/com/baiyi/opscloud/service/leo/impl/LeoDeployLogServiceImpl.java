package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeployLog;
import com.baiyi.opscloud.mapper.LeoDeployLogMapper;
import com.baiyi.opscloud.service.leo.LeoDeployLogService;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/5 20:34
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoDeployLogServiceImpl implements LeoDeployLogService {

    private final LeoDeployLogMapper leoDeployLogMapper;

    @Override
    public void add(LeoDeployLog leoDeployLog) {
        leoDeployLogMapper.insert(leoDeployLog);
    }

    @Override
    public List<LeoDeployLog> queryByDeployId(Integer deployId) {
        Example example = new Example(LeoDeployLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deployId", deployId);
        return leoDeployLogMapper.selectByExample(example);
    }

    @Override
    public List<LeoDeployLog> queryLatestLogByDeployId(Integer deployId, int size) {
        PageHelper.startPage(1, size);
        Example example = new Example(LeoDeployLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deployId", deployId);
        example.setOrderByClause("id desc");
        return leoDeployLogMapper.selectByExample(example);
    }

    @Override
    public void deleteWithDeployId(Integer deployId) {
        Example example = new Example(LeoDeployLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deployId", deployId);
        leoDeployLogMapper.deleteByExample(example);
    }

}