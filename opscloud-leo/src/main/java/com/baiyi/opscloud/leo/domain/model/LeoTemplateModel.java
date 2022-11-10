package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/1 11:43
 * @Version 1.0
 */
@Slf4j
public class LeoTemplateModel {

    /**
     * 从配置加载
     *
     * @param config
     * @return
     */
    public static TemplateConfig load(String config) {
        if (StringUtils.isEmpty(config))
            return TemplateConfig.EMPTY_TEMPLATE;
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(config);
            return new GsonBuilder().create().fromJson(JSONUtil.writeValueAsString(result), TemplateConfig.class);
        } catch (JsonSyntaxException e) {
            log.error(e.getMessage());
            return TemplateConfig.EMPTY_TEMPLATE;
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class TemplateConfig {

        private static final TemplateConfig EMPTY_TEMPLATE = TemplateConfig.builder().build();

        private Template template;

    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class Template {

        private Jenkins jenkins;
        private String name;
        // 任务目录
        private String folder;
        // 模板任务完整URL
        private String url;
        private String type;
        private String version;
        private String hash;
        private String comment;
        private List<String> tags;
        private List<LeoBaseModel.Parameter> parameters;
    }

    @Data
    @AllArgsConstructor
    public static class Jenkins {

        private LeoBaseModel.DsInstance instance;

    }


}
