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

        @Schema(name = "标签Key")
        private String tagKey;

        @Schema(name = "业务类型", example = "0")
        private Integer businessType;

        @Schema(name = "是否追加通用标签")
        private Boolean append;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema
    public static class BusinessQuery {

        @Schema(name = "标签Key")
        private String tagKey;

        @Schema(name = "业务类型", example = "1")
        private Integer businessType;

        @Schema(name = "业务id", example = "1")
        private Integer businessId;

    }
}
