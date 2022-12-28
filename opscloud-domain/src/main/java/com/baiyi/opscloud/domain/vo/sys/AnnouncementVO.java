package com.baiyi.opscloud.domain.vo.sys;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ApiModel
    public static class Announcement implements Serializable {

        private static final long serialVersionUID = 4557571372190214188L;

        private Integer id;

        /**
         * 标题
         */
        private String title;

        /**
         * 分类
         */
        private Integer kind;

        /**
         * 有效的
         */
        private Boolean isActive;

        /**
         * 内容
         */
        private String content;

        /**
         * 描述
         */
        private String comment;

    }

}
