package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.domain.model.base.YamlDump;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/4 16:47
 * @Version 1.0
 */
@Slf4j
public class LeoJobModel {

    public static JobConfig load(LeoJob leoJob) {
        return load(leoJob.getJobConfig());
    }

    /**
     * 从配置加载
     *
     * @param config
     * @return
     */
    public static JobConfig load(String config) {
        if (StringUtils.isEmpty(config)) {
            return JobConfig.EMPTY_JOB;
        }
        try {
            return YamlUtil.loadAs(config, JobConfig.class);
        } catch (Exception e) {
            throw new LeoJobException("转换配置文件错误: {}", e.getMessage());
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobConfig extends YamlDump {
        private static final JobConfig EMPTY_JOB = JobConfig.builder().build();
        private Job job;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Job {
        @Schema(description = "gitLab配置")
        private LeoBaseModel.GitLab gitLab;
        @Schema(description = "nexus配置")
        private LeoBaseModel.Nexus nexus;
        private String name;
        @Schema(description = "构建配置")
        private Build build;
        @Schema(description = "部署配置")
        private Deploy deploy;
        @Schema(description = "容器注册表配置")
        private CR cr;

        @Schema(description = "代码扫描 <不支持>")
        private Sonar sonar;

        @Schema(description = "通知配置: 多个 <不支持>")
        private List<LeoBaseModel.Notify> notifies;
        private String comment;
        private List<String> tags;

        @Schema(description = "任务参数")
        private List<LeoBaseModel.Parameter> parameters;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Build {
        private Version version;
        private BuildTools tools;
        private BuildProject project;
        private String type;
        private LeoBaseModel.Notify notify;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "构建项目配置")
    public static class BuildProject {
        @Schema(description = "组")
        private String   groupId;
        @Schema(description = "组件")
        private String  artifactId;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "构建工具")
    public static class BuildTools {
        private BuildToolsVersion version;
        @Schema(description = "工具类型: gradle、maven")
        private String type;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "获取版本信息的文件")
    public static class BuildToolsVersion {
        private String file;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Deploy {
        private LeoBaseModel.Notify notify;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Version {
        private String prefix;
        private String suffix;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sonar {
        private Boolean enabled;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "容器注册表")
    public static class CR {
        @Schema(description = "容器注册表类型: ACR、ECR")
        private String type;
        private CRInstance instance;
        private Cloud cloud;
        private Repo repo;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "容器注册表实例")
    public static class CRInstance {
        private String regionId;
        private String id;
        private String name;
        private String url;
    }

    /**
     * 云
     */
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cloud {
        private String uuid;
        private String name;
    }

    /**
     * 容器注册表
     */
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Repo {
        private String id;
        private String name;
        private String namespace;
    }

}