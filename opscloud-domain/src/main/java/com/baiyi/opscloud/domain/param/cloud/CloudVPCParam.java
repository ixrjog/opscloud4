package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:03 下午
 * @Version 1.0
 */
public class CloudVPCParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "云类型")
        private Integer cloudType;

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "扩展属性",example = "1")
        private Integer extend;

        @ApiModelProperty(value = "地区id")
        private String regionId;

        @ApiModelProperty(value = "是否有效", example = "1")
        private Integer isActive;

        @ApiModelProperty(value = "是否被删除", example = "0")
        private Integer isDeleted;

        @ApiModelProperty(value = "可用区id列表")
        private List<String> zoneIds;
    }
}
