package com.baiyi.opscloud.domain.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema
    public static class UserSetting {

        @Schema(description = "组名称")
        private String settingGroup;

        @Schema(description = "用户设置项")
        private Map<String, String> settingMap;

    }

}