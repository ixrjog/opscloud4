package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
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
    @ApiModel
    public static class UpdateDocument implements Serializable {

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
    @ApiModel
    public static class UpdateDocumentZone implements Serializable {

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
    @ApiModel
    public static class AddDocument implements Serializable {

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
    @ApiModel
    public static class DocumentZoneQuery {

        @ApiModelProperty(value = "装载区域")
        @NotNull(message = "必须指定装载区域")
        private String mountZone;

        @ApiModelProperty(value = "词典")
        private Map<String, String> dict;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class DocumentZonePageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;
        @ApiModelProperty(value = "有效")
        private Boolean isActive;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class DocumentPageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;
        @ApiModelProperty(value = "有效")
        private Boolean isActive;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DocumentQuery {

        @ApiModelProperty(value = "关键字")
        private String documentKey;

        @ApiModelProperty(value = "词典")
        private Map<String, String> dict;

    }

}
