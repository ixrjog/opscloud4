package com.baiyi.opscloud.leo.interceptor.rule.impl;

import com.baiyi.opscloud.leo.domain.model.LeoRuleModel;
import com.baiyi.opscloud.leo.interceptor.rule.BaseLeoRuleExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

/**
 * 按每月时间范围来匹配
 *
 * @Author baiyi
 * @Date 2023/1/11 09:20
 * @Version 1.0
 */
@Slf4j
@Component
public class LeoRuleExpressionWithMonthly extends BaseLeoRuleExpression {

    public String getType() {
        return "MONTHLY";
    }

    /**
     * 解析表达式
     *
     * @param expression
     * @return 命中
     */
    public boolean parse(LeoRuleModel.Expression expression) {
        Calendar calendar = Calendar.getInstance();
        // 本月第几天
        final int nowDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        List<String> beginArgs = getExpressionArgs(expression.getBegin());
        List<String> endArgs = getExpressionArgs(expression.getEnd());
        int beginDayOfMonth = Integer.parseInt(beginArgs.get(0));
        int endDayOfMonth = Integer.parseInt(endArgs.get(0));

        // 命中开始时间
        boolean hitBegin = false;
        // 命中结束时间
        boolean hitEnd = false;

        if (nowDayOfMonth >= beginDayOfMonth) {
            // 比较时间
            if (nowDayOfMonth == beginDayOfMonth) {
                LocalTime beginTime = parse(beginArgs.get(1));
                hitBegin = LocalTime.now().isAfter(beginTime);
            } else {
                hitBegin = true;
            }
        }
        log.info("开始时间: hitBeginTime={}", hitBegin);
        if (!hitBegin) {
            return false;
        }

        if (nowDayOfMonth <= endDayOfMonth) {
            // 比较时间
            if (nowDayOfMonth == endDayOfMonth) {
                LocalTime endTime = parse(endArgs.get(1));
                hitEnd = LocalTime.now().isBefore(endTime);
            } else {
                hitEnd = true;
            }
        }

        log.info("结束时间: hitEndTime={}", hitEnd);
        return hitEnd;
    }

}
