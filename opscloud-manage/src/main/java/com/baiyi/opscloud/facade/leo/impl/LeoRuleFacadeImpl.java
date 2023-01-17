package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.param.leo.LeoRuleParam;
import com.baiyi.opscloud.domain.vo.leo.LeoRuleVO;
import com.baiyi.opscloud.facade.leo.LeoRuleFacade;
import com.baiyi.opscloud.packer.leo.LeoRulePacker;
import com.baiyi.opscloud.service.leo.LeoRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/1/10 17:50
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeoRuleFacadeImpl implements LeoRuleFacade {

    private final LeoRuleService leoRuleService;

    private final LeoRulePacker leoRulePacker;

    @Override
    public DataTable<LeoRuleVO.Rule> queryLeoRulePage(LeoRuleParam.RulePageQuery pageQuery) {
        DataTable<LeoRule> table = leoRuleService.queryRulePage(pageQuery);
        List<LeoRuleVO.Rule> data = BeanCopierUtil.copyListProperties(table.getData(), LeoRuleVO.Rule.class).stream()
                .peek(leoRulePacker::wrap).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void updateLeoRule(LeoRuleParam.UpdateRule updateRule) {
        LeoRule saveLeoRule = BeanCopierUtil.copyProperties(updateRule, LeoRule.class);
        leoRuleService.updateByPrimaryKeySelective(saveLeoRule);
    }

    @Override
    public void addLeoRule(LeoRuleParam.AddRule addRule) {
        LeoRule leoRule = BeanCopierUtil.copyProperties(addRule, LeoRule.class);
        leoRuleService.add(leoRule);
    }

    @Override
    public void deleteLeoRuleById(int ruleId) {
        leoRuleService.deleteById(ruleId);
    }

}
