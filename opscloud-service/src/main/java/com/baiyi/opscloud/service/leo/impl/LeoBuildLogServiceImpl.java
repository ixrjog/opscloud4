package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildLog;
import com.baiyi.opscloud.mapper.opscloud.LeoBuildLogMapper;
import com.baiyi.opscloud.service.leo.LeoBuildLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/9 14:02
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoBuildLogServiceImpl implements LeoBuildLogService {

    private final LeoBuildLogMapper leoBuildLogMapper;

    @Override
    public void add(LeoBuildLog leoBuildLog) {
        leoBuildLogMapper.insert(leoBuildLog);
    }

    @Override
    public List<LeoBuildLog> queryByBuildId(Integer buildId) {
        Example example = new Example(LeoBuildLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildId", buildId);
        return leoBuildLogMapper.selectByExample(example);
    }

    @Override
    public List<LeoBuildLog> queryLatestLogByBuildId(Integer buildId, int size) {
        Page page = PageHelper.startPage(1, size);
        Example example = new Example(LeoBuildLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildId", buildId);
        example.orderBy("id desc");
        return leoBuildLogMapper.selectByExample(example);
    }

}
