package com.baiyi.opscloud.leo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/11/4 16:48
 * @Version 1.0
 */
public class LeoModel {

    @Data
    @AllArgsConstructor
    public static class Parameter {

        private String name;
        private String value;
        private String description;

    }

    @Data
    @AllArgsConstructor
    public static class DsInstance {

        private String name;
        private String uuid;

    }

}
