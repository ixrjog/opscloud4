package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.config.PrometheusConfig;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/25 9:57 上午
 * @Version 1.0
 */
@Component
public class PrometheusUtils {

    private static PrometheusConfig prometheusConfig;

    @Autowired
    public void setPrometheusConfig(PrometheusConfig prometheusConfig) {
        PrometheusUtils.prometheusConfig = prometheusConfig;
    }

    public interface Attributes {
        String ENABLE = "enable";
        String JOB_NAME = "job_name";
        String METRICS_PATH = "metrics_path";
        String CONFIG_PATH = "config_path";
    }

    public static boolean isEnable(Map<String, String> attributeMap) {
        if (!attributeMap.containsKey(PrometheusUtils.Attributes.ENABLE))
            return false;
        return "true".equalsIgnoreCase(attributeMap.get(PrometheusUtils.Attributes.ENABLE));
    }


    public static String getJobName(String name, Map<String, String> attributeMap) {
        if (attributeMap.containsKey(Attributes.JOB_NAME) && StringUtils.isNotBlank(attributeMap.get(Attributes.JOB_NAME)))
            return attributeMap.get(Attributes.JOB_NAME);
        return name;
    }

    public static String getMetricsPath(String name, Map<String, String> attributeMap) {
        if (attributeMap.containsKey(Attributes.METRICS_PATH) && StringUtils.isNotBlank(attributeMap.get(Attributes.METRICS_PATH)))
            return attributeMap.get(Attributes.METRICS_PATH);
        return Joiner.on("").join("/", name, prometheusConfig.getMetricsPathSuffix());
    }

    public static String getConfigPath(String name, Map<String, String> attributeMap) {
        if (attributeMap.containsKey(Attributes.CONFIG_PATH) && StringUtils.isNotBlank(attributeMap.get(Attributes.CONFIG_PATH)))
            return attributeMap.get(Attributes.CONFIG_PATH);
        return Joiner.on("").join(prometheusConfig.getConfigPathPrefix(), "/", name, ".json");
    }
}
