package com.baiyi.opscloud.domain.param.tag;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class TagPageQuery extends PageParam {

        @ApiModelProperty(value = "标签Key")
        private String tagKey;

        @ApiModelProperty(value = "业务类型", example = "0")
        private Integer businessType;

        @ApiModelProperty(value = "是否追加通用标签")
        private Boolean append;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel
    public static class BusinessQuery {

        @ApiModelProperty(value = "标签Key")
        private String tagKey;

        @ApiModelProperty(value = "业务类型", example = "1")
        private Integer businessType;

        @ApiModelProperty(value = "业务id", example = "1")
        private Integer businessId;

    }
}
