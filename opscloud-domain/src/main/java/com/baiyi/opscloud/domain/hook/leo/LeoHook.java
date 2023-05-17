package com.baiyi.opscloud.domain.hook.leo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/5/15 09:47
 * @Version 1.0
 */
public class LeoHook {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Leo持续交付构建通知")
    public static class BuildHook extends BaseLeoHook {
        @Serial
        private static final long serialVersionUID = -1L;
        private User user;
        private GitLab gitLab;
        @Schema(description = "任务名称")
        private String name;
        private final String type = "BUILD";
        @Schema(description = "构建类型")
        private String buildType;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Leo持续交付部署通知")
    @SuperBuilder(toBuilder = true)
    public static class DeployHook extends BaseLeoHook {
        @Serial
        private static final long serialVersionUID = -1L;
        private User user;
        private Integer buildId;
        @Schema(description = "任务名称")
        private String name;
        private List<Pod> pods;
        private final String type = "DEPLOY";
        @Schema(description = "部署类型")
        private String deployType;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseLeoHook implements Serializable {
        @Serial
        private static final long serialVersionUID = -1L;
        @Schema(description = "应用名")
        private String appName;
        @Schema(description = "应用ID")
        private Integer appId;
        @Schema(description = "主键")
        private Integer id;
        @Schema(description = "项目ID")
        private Integer projectId;
        @Schema(description = "环境名称")
        private String envName;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date gmtModified;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Leo持续交付构建GitLab信息")
    public static class GitLab implements Serializable {
        @Serial
        private static final long serialVersionUID = -1L;
        private String sshUrl;
        private String branch;
        private Commit commit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Leo持续交付部署后Pod信息")
    public static class Pod implements Serializable {
        @Serial
        private static final long serialVersionUID = -1L;
        private String name;
        private String ip;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Leo持续交付构建Commit信息")
    public static class Commit implements Serializable {
        @Serial
        private static final long serialVersionUID = -1L;
        private String id;
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Leo持续交付执行用户信息")
    public static class User implements Serializable {
        @Serial
        private static final long serialVersionUID = -1L;
        private String username;
        private String displayName;
        private String email;
    }

}
