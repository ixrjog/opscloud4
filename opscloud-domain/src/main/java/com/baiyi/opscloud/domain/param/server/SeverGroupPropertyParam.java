package com.baiyi.opscloud.domain.param.server;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/6/22 11:00 上午
 * @Version 1.0
 */
public class SeverGroupPropertyParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PropertyParam {

        @ApiModelProperty(value = "服务器组id")
        @NotNull
        private Integer serverGroupId;

        @ApiModelProperty(value = "属性名称")
        @NotNull
        private Set<String> propertyNameSet;

        @ApiModelProperty(value = "环境类型",example = "-1")
        @NotNull
        private Integer envType;
    }
}
