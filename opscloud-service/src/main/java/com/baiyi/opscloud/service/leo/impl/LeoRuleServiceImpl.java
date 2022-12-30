package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.mapper.opscloud.LeoRuleMapper;
import com.baiyi.opscloud.service.leo.LeoRuleService;
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
    public List<LeoRule> queryAll(){
        Example example = new Example(LeoRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true);
        return leoRuleMapper.selectByExample(example);
    }

}
