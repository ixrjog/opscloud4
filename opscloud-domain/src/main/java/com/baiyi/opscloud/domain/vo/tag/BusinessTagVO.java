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

        @Schema(name = "业务类型", example = "1")
        private Integer businessType;

        @Schema(name = "业务id(优先级高)", example = "1")
        private Integer businessId;

        @Schema(name = "业务ids(优先级低)", example = "1")
        private Set<Integer> businessIds;

        @Schema(name = "标签id", example = "1")
        private Integer tagId;

        @Schema(name = "标签key")
        private Set<Integer> tagIds;

    }

}
