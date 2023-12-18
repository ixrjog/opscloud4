package com.baiyi.opscloud.domain.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/17 5:04 下午
 * @Version 1.0
 */
public class OptionsVO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @Builder
    public static class Options {
        private List<Option> options;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @Builder
    public static class Option {
        private String label;
        private Object value;
        private Object comment;
    }

}