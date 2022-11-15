package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.leo.domain.model.base.YamlDump;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:23
 * @Version 1.0
 */
@Slf4j
public class LeoBuildModel {

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    public static class BuildConfig extends YamlDump {

        private Build build;

    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class Build {

        private LeoBaseModel.GitLab gitLab;

        private LeoBaseModel.Jenkins jenkins;

        private String comment;
        // 构建参数
        private List<LeoBaseModel.Parameter> parameters;
        // 构建标签
        private List<String> tags;

    }

}
