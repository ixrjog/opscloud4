package com.baiyi.opscloud.domain.vo.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/2/22 8:52 下午
 * @Version 1.0
 */
public class BusinessTagVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BusinessTag {

        @ApiModelProperty(value = "业务类型", example = "1")
        private Integer businessType;

        @ApiModelProperty(value = "业务id(优先级高)", example = "1")
        private Integer businessId;

        @ApiModelProperty(value = "业务ids(优先级低)", example = "1")
        private Set<Integer> businessIds;

        @ApiModelProperty(value = "标签id", example = "1")
        private Integer tagId;

        @ApiModelProperty(value = "标签key")
        private Set<Integer> tagIds;

    }
}
