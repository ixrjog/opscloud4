package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/4 16:47
 * @Version 1.0
 */
@Slf4j
public class LeoJobModel {

    /**
     * 从配置加载
     *
     * @param config
     * @return
     */
    public static JobConfig load(String config) {
        if (StringUtils.isEmpty(config))
            return JobConfig.EMPTY_JOB;
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(config);
            return new GsonBuilder().create().fromJson(JSONUtil.writeValueAsString(result), JobConfig.class);
        } catch (JsonSyntaxException e) {
            log.error(e.getMessage());
            return JobConfig.EMPTY_JOB;
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class JobConfig {

        private static final JobConfig EMPTY_JOB = JobConfig.builder().build();

        private Job job;

    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class Job {

        private String version;
        private String name;
        // 代码扫描
        private Sonar sonar;
        // 通知配置
        private Notify notify;
        private String comment;
        private List<String> tags;
        // 任务参数
        private List<LeoModel.Parameter> parameters;
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class Sonar {

        private Boolean enabled;

    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class Notify {

        private String type;
        private String name;
        private Boolean atAll;

    }


}
