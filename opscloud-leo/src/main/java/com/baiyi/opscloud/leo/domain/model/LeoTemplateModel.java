package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2022/11/1 11:43
 * @Version 1.0
 */
@Slf4j
public class LeoTemplateModel {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeoTemplate {

        private static final LeoTemplate EMPTY_TEMPLATE = LeoTemplate.builder().build();

        private Template template;

        /**
         * 从配置加载
         * @param config
         * @return
         */
        public static LeoTemplate load(String config) {
            if (StringUtils.isEmpty(config))
                return LeoTemplate.EMPTY_TEMPLATE;
            try {
                Yaml yaml = new Yaml();
                Object result = yaml.load(config);
                return new GsonBuilder().create().fromJson(JSONUtil.writeValueAsString(result), LeoTemplate.class);
            } catch (JsonSyntaxException e) {
                log.error(e.getMessage());
                return LeoTemplate.EMPTY_TEMPLATE;
            }
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Template {

        private Jenkins jenkins;
        private String name;
        private String type;
        private String version;
        private String hash;
        private String comment;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Jenkins {

        private Instance instance;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Instance {

        private String name;
        private String uuid;

    }

}
