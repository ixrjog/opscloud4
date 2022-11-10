package com.baiyi.opscloud.leo.domain.model;

import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

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
    @AllArgsConstructor
    public static class BuildConfig {

        private Build build;

        public String dump() throws JsonSyntaxException {
            Yaml yaml = new Yaml();
            return yaml.dump(this);
        }

    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class Build {

        private LeoBaseModel.GitLab gitLab;

        private String comment;
        // 构建参数
        private List<LeoBaseModel.Parameter> parameters;
        // 构建标签
        private List<String> tags;

    }

}
