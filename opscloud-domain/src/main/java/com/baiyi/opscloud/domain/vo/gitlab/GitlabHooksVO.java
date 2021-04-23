package com.baiyi.opscloud.domain.vo.gitlab;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/20 11:39 上午
 * @Version 1.0
 */
public class GitlabHooksVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Webhook {
        public String ver = "1.0.0";
        private String object_kind;
        private String event_name;
        private String before;
        private String after;
        private String ref;
        private String checkout_sha;
        private int user_id;
        private String user_name;
        private String user_username;
        private String user_email;
        private String user_avatar;
        private int project_id;
        private Project project;
        private Repository repository;
        private List<Commits> commits;
        private int total_commits_count;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
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
    @ApiModel
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
    @ApiModel
    public static class Repository  {
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
    @ApiModel
    public static class Author {
        private String name;
        private String email;
    }

    /**
     * https://gitlab.xinc818.com/help/system_hooks/system_hooks.md
     */
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SystemHook implements Serializable {

        private static final long serialVersionUID = -2133573254767532104L;

        @JsonProperty("event_name")
        private String eventName;

        private String name;

        @JsonProperty("project_id")
        private Integer projectId;

        @JsonProperty("user_id")
        private Integer userId;

        @JsonProperty("group_id")
        private Integer groupId;
    }


}
