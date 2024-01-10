package com.baiyi.opscloud.tencent.exmail.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/10/12 7:01 下午
 * @Since 1.0
 */
public class ExmailParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class User {

        @Schema(description = "成员UserID。企业邮帐号名，邮箱格式", example = "bob@example.com")
        private String userid;
        @Schema(description = "成员名称")
        private String name;
        @Schema(description = "成员所属部门id列表")
        private List<Long> department;
        @Schema(description = "手机号")
        private String mobile;
        @Schema(description = "启用/禁用成员。1表示启用成员，0表示禁用成员")
        @Builder.Default
        private Integer enable = 1;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchDept {

        private String name;
        @Builder.Default
        private Integer fuzzy = 0;

    }

}