package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Nav;
import com.baiyi.opscloud.mapper.NavMapper;
import com.baiyi.opscloud.service.sys.NavService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/6/30 7:04 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class NavServiceImpl implements NavService {

    private final NavMapper navMapper;

    @Override
    public void add(Nav nav) {
        navMapper.insert(nav);
    }

    @Override
    public void update(Nav nav) {
        navMapper.updateByPrimaryKey(nav);
    }

    @Override
    public void del(Integer id) {
        navMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Nav getById(Integer id) {
        return navMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Nav> listAll() {
        Example example = new Example(Nav.class);
        return navMapper.selectByExample(example);
    }

    @Override
    public List<Nav> listActive() {
        Example example = new Example(Nav.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true);
        return navMapper.selectByExample(example);
    }

}