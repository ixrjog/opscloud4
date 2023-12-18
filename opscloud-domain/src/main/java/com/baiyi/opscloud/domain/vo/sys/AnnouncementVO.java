package com.baiyi.opscloud.domain.vo.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2022/12/28 17:25
 * @Version 1.0
 */
public class AnnouncementVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Announcement implements Serializable {

        @Serial
        private static final long serialVersionUID = 4557571372190214188L;

        private Integer id;

        @Schema(description = "标题")
        private String title;

        @Schema(description = "分类")
        private Integer kind;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "内容")
        private String content;

        @Schema(description = "描述")
        private String comment;

    }

}