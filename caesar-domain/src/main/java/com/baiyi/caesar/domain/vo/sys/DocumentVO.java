package com.baiyi.caesar.domain.vo.sys;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:39 上午
 * @Version 1.0
 */
public class DocumentVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Doc {

        private String content;
        private Map<String, String> dict;
    }
}
