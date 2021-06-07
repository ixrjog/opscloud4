package com.baiyi.caesar.domain.vo.sys;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 10:30 上午
 * @Since 1.0
 */

public class MenuVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Menu implements Serializable {
        private static final long serialVersionUID = -2841897420522344967L;
        private Integer id;
        private String title;
        private String icon;
        private Integer seq;
        private List<Child> children;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Child implements Serializable {
        private static final long serialVersionUID = -548378621998577092L;
        private Integer id;
        private Integer menuId;
        private String title;
        private String icon;
        private String path;
        private Integer seq;
    }
}