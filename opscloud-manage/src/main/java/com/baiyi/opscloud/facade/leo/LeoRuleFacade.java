package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.leo.LeoRuleParam;
import com.baiyi.opscloud.domain.vo.leo.LeoRuleVO;

/**
 * @Author baiyi
 * @Date 2023/1/10 17:50
 * @Version 1.0
 */
public interface LeoRuleFacade {

    DataTable<LeoRuleVO.Rule> queryLeoRulePage(LeoRuleParam.RulePageQuery pageQuery);

    void updateLeoRule(LeoRuleParam.UpdateRule updateRule);

    void addLeoRule(LeoRuleParam.AddRule addRule);

    void deleteLeoRuleById(int ruleId);

}
