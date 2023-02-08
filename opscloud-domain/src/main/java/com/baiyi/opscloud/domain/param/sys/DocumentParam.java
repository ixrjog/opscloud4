package com.baiyi.opscloud.domain.param.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:25 上午
 * @Version 1.0
 */
public class DocumentParam {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class DocumentZoneQuery {

        @ApiModelProperty(value = "装载区域")
        @NotNull(message = "必须指定装载区域" )
        private String mountZone;

        @ApiModelProperty(value = "词典")
        private Map<String, String> dict;

    }

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
