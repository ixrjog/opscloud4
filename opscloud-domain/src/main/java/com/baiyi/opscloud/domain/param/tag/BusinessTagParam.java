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

        @Schema(name = "业务类型", example = "1")
        private Integer businessType;

        @Schema(name = "业务对象id", example = "1")
        private Integer businessId;

        @Schema(name = "标签key")
        private Set<Integer> tagIds;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UniqueKeyQuery {

        @Schema(name = "标签Kid", example = "1")
        private Integer tagId;

        @Schema(name = "业务类型", example = "1")
        private Integer businessType;

        @Schema(name = "业务id", example = "1")
        private Integer businessId;

    }

}