package com.baiyi.opscloud.domain.vo.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/2/22 8:52 下午
 * @Version 1.0
 */
public class BusinessTagVO {

    @Data
    @Builder
    @Schema
    public static class BusinessTag {

        @Schema(description = "业务类型", example = "1")
        private Integer businessType;

        @Schema(description = "业务ID(优先级高)", example = "1")
        private Integer businessId;

        @Schema(description = "业务IDs(优先级低)", example = "1")
        private Set<Integer> businessIds;

        @Schema(description = "标签ID", example = "1")
        private Integer tagId;

        @Schema(description = "标签key")
        private Set<Integer> tagIds;

    }

}