package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.param.leo.LeoRuleParam;
import com.baiyi.opscloud.mapper.LeoRuleMapper;
import com.baiyi.opscloud.service.leo.LeoRuleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/29 18:08
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoRuleServiceImpl implements LeoRuleService {

    private final LeoRuleMapper leoRuleMapper;

    @Override
    public List<LeoRule> queryAll() {
        Example example = new Example(LeoRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true);
        return leoRuleMapper.selectByExample(example);
    }

    @Override
    public List<LeoRule> queryAllTest() {
        return leoRuleMapper.selectAll();
    }

    @Override
    public LeoRule getById(int id){
        return leoRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<LeoRule> queryRulePage(LeoRuleParam.RulePageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(LeoRule.class);
        if (pageQuery.getIsActive() != null) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        List<LeoRule> data = leoRuleMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void updateByPrimaryKeySelective(LeoRule leoRule) {
        leoRuleMapper.updateByPrimaryKeySelective(leoRule);
    }

    @Override
    public void add(LeoRule leoRule) {
        leoRuleMapper.insert(leoRule);
    }

    @Override
    public void deleteById(int id) {
        leoRuleMapper.deleteByPrimaryKey(id);
    }

}