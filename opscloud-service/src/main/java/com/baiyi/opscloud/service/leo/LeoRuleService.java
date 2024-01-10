package com.baiyi.opscloud.service.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.param.leo.LeoRuleParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/29 18:07
 * @Version 1.0
 */
public interface LeoRuleService {

    List<LeoRule> queryAll();

    List<LeoRule> queryAllTest();

    LeoRule getById(int id);

    DataTable<LeoRule> queryRulePage(LeoRuleParam.RulePageQuery pageQuery);

    void updateByPrimaryKeySelective(LeoRule leoRule);

    void add(LeoRule leoRule);

    void deleteById(int id);

}