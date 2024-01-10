package com.baiyi.opscloud.datasource.jenkins.entity;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/1/5 1:37 PM
 * @Version 1.0
 */
public class JenkinsPipeline {

    @Data
    public static class Pipeline {

        private Boolean disabled;
        private String displayName;
        private Long estimatedDurationInMillis;
        private String fullDisplayName;
        private String fullName;

    }

}