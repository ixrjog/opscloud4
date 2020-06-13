package com.baiyi.opscloud.domain.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/17 2:04 下午
 * @Version 1.0
 */
public class UserSettingParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserSetting {

        @ApiModelProperty(value = "组名称")
        private String settingGroup;

        @ApiModelProperty(value = "用户设置项")
        private Map<String, String> settingMap;
    }

}
