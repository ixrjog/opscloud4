package com.baiyi.opscloud.leo.interceptor.rule;

/**
 * @Author baiyi
 * @Date 2022/12/29 19:40
 * @Version 1.0
 */
public class RuleHelper {

//    public void verifyRule(int jobId) {
//        List<LeoRule> rules = leoRuleService.queryAll();
//        LeoJob leoJob = leoJobService.getById(jobId);
//        Env env = envService.getByEnvType(leoJob.getEnvType());
//        for (LeoRule rule : rules) {
//            LeoRuleModel.RuleConfig ruleConfig = LeoRuleModel.load(rule);
//            List<String> envs = Optional.ofNullable(ruleConfig)
//                    .map(LeoRuleModel.RuleConfig::getRule)
//                    .map(LeoRuleModel.Rule::getEnvs)
//                    .orElse(Collections.emptyList());
//            if (!CollectionUtils.isEmpty(envs)) {
//                if(envs.stream().anyMatch(e-> e.equalsIgnoreCase(env.getEnvName()))){
//                    throw new LeoInterceptorException("非应用管理员禁止操作生产环境！");
//                }
//            }
//        }
//    }
//
//    public void rule( LeoRuleModel.RuleConfig ruleConfig){
//        List<LeoRule> rules = leoRuleService.queryAll();
//        LeoJob leoJob = leoJobService.getById(jobId);
//        Env env = envService.getByEnvType(leoJob.getEnvType());
//        for (LeoRule rule : rules) {
//            LeoRuleModel.RuleConfig ruleConfig = LeoRuleModel.load(rule);
//            List<String> envs = Optional.ofNullable(ruleConfig)
//                    .map(LeoRuleModel.RuleConfig::getRule)
//                    .map(LeoRuleModel.Rule::getEnvs)
//                    .orElse(Collections.emptyList());
//            if (!CollectionUtils.isEmpty(envs)) {
//                if(envs.stream().anyMatch(e-> e.equalsIgnoreCase(env.getEnvName()))){
//                    throw new LeoInterceptorException("非应用管理员禁止操作生产环境！");
//                }
//            }
//        }
//    }

}
