package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/18 11:14 上午
 * @Version 1.0
 */
public class CloudImageParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "云类型")
        private Integer cloudType;

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "地区id")
        private String regionId;

        @ApiModelProperty(value = "是否有效", example = "1")
        private Integer isActive;

        @ApiModelProperty(value = "是否被删除", example = "0")
        private Integer isDeleted;
    }

}
