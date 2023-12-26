package com.baiyi.opscloud.domain.param.tag;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:08 下午
 * @Version 1.0
 */
public class TagParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class TagPageQuery extends PageParam {

        @Schema(description = "标签Key")
        private String tagKey;

        @Schema(description = "业务类型", example = "0")
        private Integer businessType;

        @Schema(description = "是否追加通用标签")
        private Boolean append;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema
    public static class BusinessQuery {

        @Schema(description = "标签Key")
        private String tagKey;

        @Schema(description = "业务类型", example = "1")
        private Integer businessType;

        @Schema(description = "业务ID", example = "1")
        private Integer businessId;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema
    public static class Tag  {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "业务类型", example = "0")
        private Integer businessType;

        @Schema(description = "标签key")
        private String tagKey;

        @Schema(description = "颜色值")
        private String color;

        @Schema(description = "描述")
        private String comment;

    }

}