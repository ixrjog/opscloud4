package com.baiyi.opscloud.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class Profiles {

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "用户ID")
        private Integer userId;

        @ApiModelProperty(value = "终端设置")
        @Builder.Default
        private Terminal terminal = Terminal.builder().build();

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Terminal {

        @ApiModelProperty(value = "主题")
        @Builder.Default
        private Theme theme = Theme.builder().build();
        @ApiModelProperty(value = "行数")
        @Builder.Default
        private Integer rows = 30;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Theme {

        @ApiModelProperty(value = "字体颜色")
        @Builder.Default
        private String foreground = "#090909";

        @ApiModelProperty(value = "背景色")
        @Builder.Default
        private String background = "#FFFFFF";

        @ApiModelProperty(value = "光标色")
        @Builder.Default
        private String cursor = "#090909";

    }

}
