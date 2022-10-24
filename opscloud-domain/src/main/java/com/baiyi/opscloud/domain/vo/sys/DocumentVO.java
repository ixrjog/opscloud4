package com.baiyi.opscloud.domain.vo.sys;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
    public static class Doc implements Serializable {

        private static final long serialVersionUID = -6463097211186364301L;

        private String content;
        private Map<String, String> dict;
    }

}
