package com.baiyi.opscloud.datasource.jenkins.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2022/1/5 10:47 AM
 * @Version 1.0
 */
public class JenkinsUser {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User implements Serializable {

        @Serial
        private static final long serialVersionUID = 967178899220442346L;

        private String id;
        private String fullName;
        private String avatar;
        //   private String name;
        private String email;

    }

    @Data
    public static class Permission {

        private Boolean administrator;
        private Credential credential;
        private Pipeline pipeline;

    }

    @Data
    public static class Credential {

        private Boolean view;
        private Boolean create;
        private Boolean update;
        private Boolean manageDomains;
        private Boolean delete;

    }

    @Data
    public static class Pipeline {

        private Boolean read;
        private Boolean stop;
        private Boolean start;
        private Boolean create;
        private Boolean configure;

    }

}