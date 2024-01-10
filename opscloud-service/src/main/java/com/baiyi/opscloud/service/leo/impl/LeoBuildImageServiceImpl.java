package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;
import com.baiyi.opscloud.mapper.LeoBuildImageMapper;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/22 17:14
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoBuildImageServiceImpl implements LeoBuildImageService {

    private final LeoBuildImageMapper leoBuildImageMapper;

    @Override
    public void add(LeoBuildImage leoBuildImage) {
        leoBuildImageMapper.insert(leoBuildImage);
    }

    @Override
    public void updateByPrimaryKeySelective(LeoBuildImage leoBuildImage) {
        leoBuildImageMapper.updateByPrimaryKeySelective(leoBuildImage);
    }

    @Override
    public LeoBuildImage getByUniqueKey(int buildId, String image) {
        Example example = new Example(LeoBuildImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildId", buildId)
                .andEqualTo("image", image);
        return leoBuildImageMapper.selectOneByExample(example);
    }

    @Override
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, unless = "#result == null")
    public LeoBuildImage getByImage(String image) {
        Example example = new Example(LeoBuildImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("image", image);
        return leoBuildImageMapper.selectOneByExample(example);
    }

    @Override
    public LeoBuildImage findBuildImage(int jobId, String image) {
        Page<?> page = PageHelper.startPage(1, 1);
        Example example = new Example(LeoBuildImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId", jobId)
                .andEqualTo("image", image);
        return leoBuildImageMapper.selectOneByExample(example);
    }

    @Override
    public List<LeoBuildImage> queryImageWithJobIdAndImage(int jobId, String image) {
        Example example = new Example(LeoBuildImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId", jobId)
                .andEqualTo("image", image);
        return leoBuildImageMapper.selectByExample(example);
    }

}