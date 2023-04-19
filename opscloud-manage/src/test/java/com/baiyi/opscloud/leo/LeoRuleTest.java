package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.leo.interceptor.rule.RuleValidator;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.leo.LeoRuleService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/1/10 13:54
 * @Version 1.0
 */
public class LeoRuleTest extends BaseUnit {

    @Resource
    private RuleValidator ruleValidator;

    @Resource
    private LeoRuleService leoRuleService;

    @Resource
    private LeoJobService leoJobService;

    @Test
    void test() {
        // List<LeoRule> rules = leoRuleService.queryAllTest();
        List<LeoRule> rules = Lists.newArrayList(leoRuleService.getById(7));
        LeoJob leoJob = leoJobService.getById(4);
        for (LeoRule rule : rules) {
            ruleValidator.verify(leoJob, rule);
        }

    }

}
