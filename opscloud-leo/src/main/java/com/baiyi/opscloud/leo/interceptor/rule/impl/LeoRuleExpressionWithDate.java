package com.baiyi.opscloud.leo.interceptor.rule.impl;

import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.interceptor.rule.BaseLeoRuleExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 按具体时间范围来匹配
 *
 * @Author baiyi
 * @Date 2023/1/10 10:32
 * @Version 1.0
 */
@Slf4j
@Component
public class LeoRuleExpressionWithDate extends BaseLeoRuleExpression {

    public String getType() {
        return "DATE";
    }

    public boolean parse(LeoRuleModel.Expression expression) {
        Date beginDate = TimeUtil.gmtToDate(expression.getBegin());
        Date endDate = TimeUtil.gmtToDate(expression.getEnd());
        Date nowDate = new Date();
        boolean hitBegin = nowDate.after(beginDate);
        log.info("开始时间: hitBeginTime={}", hitBegin);
        boolean hitEnd = nowDate.before(endDate);
        log.info("结束时间: hitEndTime={}", hitEnd);
        return hitBegin && hitEnd;
    }

}
