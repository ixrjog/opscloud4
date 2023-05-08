package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.domain.model.base.YamlDump;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:23
 * @Version 1.0
 */
@Slf4j
public class LeoBuildModel {

    public static BuildConfig load(LeoBuild leoBuild) {
        return load(leoBuild.getBuildConfig());
    }

    /**
     * 从配置加载
     *
     * @param config
     * @return
     */
    public static BuildConfig load(String config) {
        if (StringUtils.isEmpty(config)) {
            return BuildConfig.EMPTY_BUILD;
        }
        try {
            return YamlUtil.loadAs(config, BuildConfig.class);
        } catch (Exception e) {
            throw new LeoJobException("转换配置文件错误: {}", e.getMessage());
        }
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BuildConfig extends YamlDump {
        private static final BuildConfig EMPTY_BUILD = BuildConfig.builder().build();
        private Build build;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Build {

        @Schema(description = "gitLab配置")
        private LeoBaseModel.GitLab gitLab;
        @Schema(description = "jenkins配置")
        private LeoBaseModel.Jenkins jenkins;
        @Schema(description = "nexus配置")
        private LeoBaseModel.Nexus nexus;

        @Schema(description = "自动部署配置")
        private LeoBaseModel.AutoDeploy autoDeploy;

        private LeoBaseModel.Notify notify;
        private String comment;
        @Schema(description = "构建类型: kubernetes-image, maven-deploy")
        private String type;

        @Schema(description = "字典")
        private Map<String, String> dict;
        @Schema(description = "构建参数")
        private List<LeoBaseModel.Parameter> parameters;
        @Schema(description = "构建标签")
        private List<String> tags;
    }

}