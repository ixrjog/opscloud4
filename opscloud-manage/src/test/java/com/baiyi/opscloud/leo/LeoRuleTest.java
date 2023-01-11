package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.leo.interceptor.rule.RuleHelper;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.leo.LeoRuleService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/1/10 13:54
 * @Version 1.0
 */
public class LeoRuleTest extends BaseUnit {

    @Resource
    private RuleHelper ruleHelper;

    @Resource
    private LeoRuleService leoRuleService;

    @Resource
    private LeoJobService leoJobService;

    @Test
    void test() {
        List<LeoRule> rules = leoRuleService.queryAll();
        // cfront-prod
        LeoJob leoJob = leoJobService.getById(4);
        for (LeoRule rule : rules) {
            ruleHelper.verifyRule(leoJob, rule);
        }

    }

}
