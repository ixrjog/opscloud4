package com.baiyi.opscloud.domain.param.tag;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:10 下午
 * @Version 1.0
 */
public class BusinessTagParam {

    @Data
    @Builder
    @Schema
    public static class UpdateBusinessTags implements BaseBusiness.IBusiness {

        @Schema(description = "业务类型", example = "1")
        private Integer businessType;

        @Schema(description = "业务对象ID", example = "1")
        private Integer businessId;

        @Schema(description = "标签key")
        private Set<Integer> tagIds;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UniqueKeyQuery {

        @Schema(description = "标签ID", example = "1")
        private Integer tagId;

        @Schema(description = "业务类型", example = "1")
        private Integer businessType;

        @Schema(description = "业务ID", example = "1")
        private Integer businessId;

    }

}