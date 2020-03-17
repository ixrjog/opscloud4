package com.baiyi.opscloud.domain.param.tag;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:08 下午
 * @Version 1.0
 */
public class TagParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "标签Key")
        private String tagKey;

        @ApiModelProperty(value = "服务器id", example = "1")
        private Integer serverId;

    }

    @Data
    @NoArgsConstructor
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
