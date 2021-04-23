package com.baiyi.opscloud.facade.prometheus;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.bo.prometheus.PrometheusBO;
import com.baiyi.opscloud.common.util.JSONFormat;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;


/**
 * @Author baiyi
 * @Date 2021/2/25 10:30 上午
 * @Version 1.0
 */
class PrometheusFacadeTest extends BaseUnit {

    @Resource
    private PrometheusFacade prometheusFacade;

    @Test
    void test() {
        List<PrometheusBO.Job> jobs = prometheusFacade.getJobs();
        System.err.println(JSON.toJSON(jobs));
    }

    @Test
    void test3() {
        Map<String, List<PrometheusBO.Target>> targetMap = prometheusFacade.getTargetMap();
        targetMap.keySet().forEach(k -> {
            System.err.println(JSONFormat.format(targetMap.get(k)));
        });
    }

    @Test
    void test4() {
        prometheusFacade.writeConfig();
    }

    @Test
    void test5() {
        prometheusFacade.writeConfigFiles();
    }

}