package com.baiyi.opscloud.domain.param.notify.gitlab;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/28 4:27 下午
 * @Version 1.0
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
public class GitLabNotifyParam {

    /**
     * https://gitlab.xxx.com/help/system_hooks/system_hooks.md
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SystemHook {

        private final String ver = "1.0.0";
        private String object_kind;
        private Integer id;
        private String event_name;
        private String name;
        private String before;
        private String after;
        private String ref;
        private List<String> refs;
        // @JsonProperty("checkout_sha")
        private String checkout_sha;
        private Integer user_id;
        private String user_name; // 显示名
        private String username; // 显示名
        private String user_username; // 登录名
        private String user_email;
        private String user_avatar;
        private Integer group_id;
        private Integer project_id;
        private Project project;
        private Repository repository;
        private List<Commits> commits;
        private int total_commits_count;
        private String key;
        private ObjectAttributes object_attributes;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Project {
        private long id;
        private String name;
        private String description;
        private String web_url;
        private String avatar_url;
        private String git_ssh_url;
        private String git_http_url;
        private String namespace;
        private int visibility_level;
        private String path_with_namespace;
        private String default_branch;
        private String homepage;
        private String url;
        private String ssh_url;
        private String http_url;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Commits {
        private String id;
        private String message;
        private String timestamp;
        private String url;
        private List<String> added;
        private List<String> modified;
        private List<String> removed;
        private Author author;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Repository {
        private String name;
        private String url;
        private String description;
        private String homepage;
        private String git_http_url;
        private String git_ssh_url;
        private int visibility_level;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Author {
        private String name;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ObjectAttributes {
        private int id;
        @Schema(description = "目标分支")
        private String target_branch;
        @Schema(description = "源分支")
        private String source_branch;
        private int source_project_id;

    }

}