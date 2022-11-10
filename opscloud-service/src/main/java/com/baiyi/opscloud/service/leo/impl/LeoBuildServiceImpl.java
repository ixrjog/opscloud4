package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.mapper.opscloud.LeoBuildMapper;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:03
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoBuildServiceImpl implements LeoBuildService {

    private final LeoBuildMapper leoBuildMapper;

    @Override
    public void add(LeoBuild leoBuild) {
        leoBuildMapper.insert(leoBuild);
    }

    @Override
    public LeoBuild getById(Integer id) {
        return leoBuildMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(LeoBuild leoBuild) {
        leoBuildMapper.updateByPrimaryKeySelective(leoBuild);
    }

    @Override
    public int getMaxBuildNumberWithJobId(Integer jobId) {
        Integer maxBuildNumber = leoBuildMapper.getMaxBuildNumberWithJobId(jobId);
        return maxBuildNumber == null ? 0 : maxBuildNumber;
    }

}
