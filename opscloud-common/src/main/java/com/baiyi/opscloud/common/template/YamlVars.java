package com.baiyi.opscloud.common.template;

import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import lombok.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

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

        public String dump() throws JsonSyntaxException {
            DumperOptions dumperOptions = new DumperOptions();
            // 设置层级显示
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            // 显示开始结束分隔符
            dumperOptions.setExplicitStart(true);
            dumperOptions.setExplicitEnd(true);
            // 缩进
            dumperOptions.setIndent(2);

            Yaml yaml = new Yaml(dumperOptions);
            return yaml.dump(this);
        }
    }

}