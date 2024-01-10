package com.baiyi.opscloud.common.constants.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2023/2/2 16:28
 * @Version 1.0
 */
@Getter
public enum UserProfileKeyEnum {

    /**
     * 终端主题
     */
    TERMINAL_THEME_FOREGROUND("terminal.theme.foreground", "#090909"),
    TERMINAL_THEME_BACKGROUND("terminal.theme.background", "#FFFFFF"),
    TERMINAL_THEME_CURSOR("terminal.theme.cursor", "#090909"),
    TERMINAL_ROWS("terminal.rows", "30");

    private final String key;
    private final String value;

    UserProfileKeyEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static UserProfileKeyEnum of(String key) {
        return Arrays.stream(UserProfileKeyEnum.values()).filter(typeEnum -> typeEnum.key.equals(key)).findFirst().orElse(null);
    }

}