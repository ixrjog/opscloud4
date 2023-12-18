package com.baiyi.opscloud.domain.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/2/2 15:11
 * @Version 1.0
 */
public class UserProfileVO {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Profiles {

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "用户ID")
        private Integer userId;

        @Schema(description = "终端设置")
        @Builder.Default
        private Terminal terminal = Terminal.builder().build();

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Terminal {

        @Schema(description = "主题")
        @Builder.Default
        private Theme theme = Theme.builder().build();
        @Schema(description = "行数")
        @Builder.Default
        private Integer rows = 30;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Theme {

        @Schema(description = "字体颜色")
        @Builder.Default
        private String foreground = "#090909";

        @Schema(description = "背景色")
        @Builder.Default
        private String background = "#FFFFFF";

        @Schema(description = "光标色")
        @Builder.Default
        private String cursor = "#090909";

    }

}