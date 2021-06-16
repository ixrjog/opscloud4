package com.baiyi.caesar.domain.param.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:25 上午
 * @Version 1.0
 */
public class DocumentParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DocumentQuery {

        @ApiModelProperty(value = "关键字")
        private String documentKey;

        @ApiModelProperty(value = "词典")
        private Map<String, String> dict;

    }
}
