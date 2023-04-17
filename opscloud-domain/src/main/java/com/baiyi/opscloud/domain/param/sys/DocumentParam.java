package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:25 上午
 * @Version 1.0
 */
public class DocumentParam {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateDocument implements Serializable {

        @Serial
        private static final long serialVersionUID = -8475762526256407735L;

        private Integer id;
        private String name;
        private String mountZone;
        private String icon;
        private Integer seq;
        private String documentKey;
        private Integer documentType;
        private Boolean isActive;
        private String comment;
        private String content;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateDocumentZone implements Serializable {
        @Serial
        private static final long serialVersionUID = -1413511527437308526L;
        private Integer id;
        private String name;
        private Boolean isActive;
        private String comment;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddDocument implements Serializable {

        @Serial
        private static final long serialVersionUID = 730778424833527441L;

        private Integer id;
        private String name;
        private String mountZone;
        private String icon;
        private Integer seq;
        private String documentKey;
        private Integer documentType;
        private Boolean isActive;
        private String comment;
        private String content;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DocumentZoneQuery {

        @Schema(description = "文档挂载区域")
        @NotNull(message = "必须指定文档挂载区域")
        private String mountZone;

        @Schema(description = "词典")
        private Map<String, String> dict;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class DocumentZonePageQuery extends PageParam {

        @Schema(description = "关键字查询")
        private String queryName;
        @Schema(description = "有效")
        private Boolean isActive;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class DocumentPageQuery extends PageParam {

        @Schema(description = "关键字查询")
        private String queryName;

        @Schema(description = "文档挂载区域")
        private String mountZone;

        @Schema(description = "有效")
        private Boolean isActive;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class DocumentQuery {

        @Schema(description = "关键字")
        private String documentKey;

        @Schema(description = "词典")
        private Map<String, String> dict;

    }

}