package com.baiyi.opscloud.facade.prometheus;

import com.baiyi.opscloud.bo.prometheus.PrometheusBO;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.prometheus.PrometheusParam;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/24 5:08 下午
 * @Version 1.0
 */
public interface PrometheusFacade {

    Map<String, List<PrometheusBO.Target>> getTargetMap();

    BusinessWrapper<String> getPrometheusJobTemplate();

    BusinessWrapper<String> getGroupTarget(Integer serverGroupId);

    BusinessWrapper<String> getPrometheusJobTemplate(Integer serverGroupId);

    List<PrometheusBO.Job> getJobs();

    BusinessWrapper<Boolean> createPrometheusConfigTask();

    void writeConfig();

    void writeConfigFiles();

    BusinessWrapper<Boolean> savePrometheusConfig(PrometheusParam.SaveConfig param);

    BusinessWrapper<String> queryPrometheusConfig();
}
