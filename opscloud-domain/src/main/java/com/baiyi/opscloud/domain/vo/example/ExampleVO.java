package com.baiyi.opscloud.domain.vo.example;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2023/9/14 14:13
 * @Version 1.0
 */
public class ExampleVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class HelloWorld {

        public static final HelloWorld EXAMPLE = ExampleVO.HelloWorld.builder().build();

        @Builder.Default
        private String msg = "Welcome to Baiyi's operations world";

        @Builder.Default
        private Date requestTime = new Date();

    }

}