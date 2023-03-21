package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoRule;
import com.baiyi.opscloud.domain.vo.leo.LeoRuleVO;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Date;
import java.util.List;

import static com.baiyi.opscloud.leo.domain.model.LeoRuleModel.RuleConfig.EMPTY_RULE;

/**
 * @Author baiyi
 * @Date 2022/12/29 18:13
 * @Version 1.0
 */
public class LeoRuleModel {

    public static LeoRuleModel.RuleConfig load(LeoRule leoRule) {
        return load(leoRule.getRuleConfig());
    }

    public static LeoRuleModel.RuleConfig load(LeoRuleVO.Rule rule) {
        return load(rule.getRuleConfig());
    }

    /**
     * 从配置加载
     *
     * @param config
     * @return
     */
    public static LeoRuleModel.RuleConfig load(String config) {
        if (StringUtils.isEmpty(config)) {
            return EMPTY_RULE;
        }
        try {
            Representer representer = new Representer(new DumperOptions());
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(new Constructor(LeoRuleModel.RuleConfig.class), representer);
            return yaml.loadAs(config, LeoRuleModel.RuleConfig.class);
        } catch (Exception e) {
            throw new LeoJobException("转换配置文件错误: err={}", e.getMessage());
        }
    }


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RuleConfig {

        public static final LeoRuleModel.RuleConfig EMPTY_RULE = LeoRuleModel.RuleConfig.builder().build();

        private Rule rule;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Rule {

        private Expression expression;
        private List<String> tags;
        private List<String> envs;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Expression {

        private String type;
        private String begin;
        private String end;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

    @Slf4j
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateExpression {

        private Date beginDate;
        private Date endDate;
        @Builder.Default
        private Date nowDate = new Date();

        public static DateExpression build(Expression expression) {
            return DateExpression.builder()
                    .beginDate(NewTimeUtil.parse(expression.getBegin()))
                    .endDate(NewTimeUtil.parse(expression.getEnd()))
                    .build();
        }

        public boolean parse() {
            boolean hitBegin = this.nowDate.after(this.beginDate);
            log.info("开始时间: hitBeginTime={}", hitBegin);
            boolean hitEnd = this.nowDate.before(this.endDate);
            log.info("结束时间: hitEndTime={}", hitEnd);
            return hitBegin && hitEnd;
        }

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

    @Slf4j
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyExpression {

        private Date beginDate;
        private Date endDate;
        @Builder.Default
        private Date nowDate = NewTimeUtil.parse(NewTimeUtil.parse(new Date(), NewTimeUtil.TIME), NewTimeUtil.TIME);

        public static DailyExpression build(Expression expression) {
            return DailyExpression.builder()
                    .beginDate(NewTimeUtil.parse(expression.getBegin(), NewTimeUtil.TIME))
                    .endDate(NewTimeUtil.parse(expression.getEnd(), NewTimeUtil.TIME))
                    .build();
        }

        public boolean parse() {
            boolean hitBegin = this.nowDate.after(this.beginDate);
            log.info("开始时间: hitBeginTime={}", hitBegin);
            boolean hitEnd = this.nowDate.before(this.endDate);
            log.info("结束时间: hitEndTime={}", hitEnd);
            return hitBegin && hitEnd;
        }

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }

    }

}
