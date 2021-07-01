package com.baiyi.opscloud.domain.param.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class UpdateBusinessTags {

        @ApiModelProperty(value = "业务类型", example = "1")
        private Integer businessType;

        @ApiModelProperty(value = "业务对象id", example = "1")
        private Integer businessId;

        @ApiModelProperty(value = "标签key")
        private Set<Integer> tagIds;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UniqueKeyQuery {

        @ApiModelProperty(value = "标签Kid", example = "1")
        private Integer tagId;

        @ApiModelProperty(value = "业务类型", example = "1")
        private Integer businessType;

        @ApiModelProperty(value = "业务id", example = "1")
        private Integer businessId;

    }
}
