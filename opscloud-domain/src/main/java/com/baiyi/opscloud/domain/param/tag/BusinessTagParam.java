package com.baiyi.opscloud.domain.param.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:10 下午
 * @Version 1.0
 */
public class BusinessTagParam {

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
