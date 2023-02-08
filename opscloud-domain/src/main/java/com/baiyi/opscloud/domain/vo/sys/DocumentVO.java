package com.baiyi.opscloud.domain.vo.sys;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:39 上午
 * @Version 1.0
 */
public class DocumentVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Doc implements Serializable {

        private static final long serialVersionUID = -6463097211186364301L;
        private String name;
        private String icon;
        private String documentKey;
        private String content;
        private Map<String, String> dict;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class DocZone implements Serializable {

        private static final long serialVersionUID = 4925220340334652936L;

        public static final DocZone EMPTY = DocZone.builder().build();

        @Builder.Default
        private Zone zone = Zone.builder().build();

        @Builder.Default
        private List<Doc> docs = Collections.emptyList();

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Zone implements Serializable {

        private static final long serialVersionUID = 4925220340334652936L;

        private Integer id;
        private String name;
        private String mountZone;
        @Builder.Default
        private Boolean isActive = false;
        private String comment;

    }

}
