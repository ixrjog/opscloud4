package com.baiyi.caesar.domain.vo.sys;

import com.baiyi.caesar.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 10:30 上午
 * @Since 1.0
 */

public class MenuVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Menu extends BaseVO implements Serializable {
        private static final long serialVersionUID = -2841897420522344967L;
        private Integer id;
        private String title;
        private String icon;
        private Integer seq;
        private List<MenuChild> menuChildren;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class MenuChild extends BaseVO implements Serializable {
        private static final long serialVersionUID = -548378621998577092L;
        private Integer id;
        private Integer menuId;
        private String title;
        private String icon;
        private String path;
        private Integer seq;
    }
}