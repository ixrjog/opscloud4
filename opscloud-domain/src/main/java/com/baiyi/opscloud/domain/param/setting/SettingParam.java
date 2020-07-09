package com.baiyi.opscloud.domain.param.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/6/23 1:42 下午
 * @Version 1.0
 */
public class SettingParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UpdateSettingParam {

        @ApiModelProperty(value = "配置项名称")
        @NotNull
        private String name;

        @ApiModelProperty(value = "配置值")
        @NotNull
        private String settingValue;
    }
}
