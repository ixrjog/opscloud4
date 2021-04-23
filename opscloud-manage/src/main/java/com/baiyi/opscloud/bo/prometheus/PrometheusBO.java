package com.baiyi.opscloud.bo.prometheus;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/2/24 5:06 下午
 * @Version 1.0
 */
public class PrometheusBO {

    @Data
    @Builder
    public static class Job {

        private OcServerGroup serverGroup;
        private String jobName;
        private String metricsPath;
        private String configPath;
        private String comment;

    }

    @Data
    @Builder
    public static class Target{

        private List<String> targets;
        private Labels labels;

    }

    @Data
    @Builder
    public static class Labels{

        private String group;
        private String application;

    }

}
