package com.baiyi.opscloud.domain.vo.sys;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2022/6/30 8:42 PM
 * @Since 1.0
 */
public class NavVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Nav {

        private Integer id;
        private String navTitle;
        private String navUrl;
        private String navContent;
    }
}
