package com.baiyi.opscloud.leo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/11/4 16:48
 * @Version 1.0
 */
public class LeoBaseModel {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameter {
        private String name;
        private String value;
        private String description;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DsInstance {
        private String name;
        private String uuid;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GitLab {
        private LeoBaseModel.DsInstance instance;
        private GitLabProject project;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GitLabProject {
        private String sshUrl;
        private String branch;
        private GitLabCommit commit;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GitLabCommit {

        private String id;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Jenkins {
        private LeoBaseModel.DsInstance instance;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Notify {
        public static final Notify EMPTY_NOTIFY = Notify.builder().build();
        private String type;
        private String name;
        private Boolean atAll;
    }

}