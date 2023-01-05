package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/7/1 1:51 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JenkinsConfig extends BaseDsConfig {

    private Jenkins jenkins;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Jenkins {

        private String version;
        private String url;
        private String ip;
        private Security security;
        private String name;
        private String username;
        private String token;
        private Template template;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Template {

        public static final Set<String> DEF_FOLDERS = Sets.newHashSet("templates");

        public static final String DEF_PREFIX = "tpl_";

        private Set<String> folders;
        private String prefix;
        private String _class;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Security {

        private Boolean crumbFlag;

    }

}
