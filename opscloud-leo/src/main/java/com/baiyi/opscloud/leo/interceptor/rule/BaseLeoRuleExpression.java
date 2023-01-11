package com.baiyi.opscloud.leo.interceptor.rule;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/1/10 10:25
 * @Version 1.0
 */
public abstract class BaseLeoRuleExpression implements IRuleExpression, InitializingBean {

    protected LocalTime parse(String timeStr) {
        return LocalTime.parse(timeStr);
    }

    protected List<String> getExpressionArgs(String str) {
        return Arrays.stream(str.split(" ")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    public void afterPropertiesSet() throws Exception {
        LeoRuleExpressionFactory.register(this);
    }

}
