package com.baiyi.opscloud.domain.vo.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 8:52 下午
 * @Version 1.0
 */
public class OcBusinessTagVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BusinessTag {

        @ApiModelProperty(value = "业务类型", example = "1")
        private Integer businessType;

        @ApiModelProperty(value = "业务id", example = "1")
        private Integer businessId;

        @ApiModelProperty(value = "标签id", example = "1")
        private Integer tagId;

        @ApiModelProperty(value = "标签key")
        private List<Integer> tagIds;

    }
}
