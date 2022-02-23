package com.baiyi.opscloud.common.template;

import com.google.common.collect.Maps;
import lombok.*;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/12/6 7:23 PM
 * @Version 1.0
 */
public class YamlVars {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Vars {
        public static Vars EMPTY = Vars.builder().build();

        @Builder.Default
        private Map<String, String> vars = Maps.newHashMap();
    }
}
