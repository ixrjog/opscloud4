package com.baiyi.opscloud.ansible.param;

import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/8/31 2:03 下午
 * @Version 1.0
 */
public class AnsibleParam {

    @Data
    @Builder
    public static class ExtraVars {

        public static ExtraVars EMPTY = ExtraVars.builder().build();

        @Builder.Default
        private Map<String, String> vars = Maps.newHashMap();

    }

}
