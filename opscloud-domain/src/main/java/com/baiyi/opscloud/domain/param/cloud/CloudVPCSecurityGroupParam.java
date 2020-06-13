package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/25 10:33 上午
 * @Version 1.0
 */
public class CloudVPCSecurityGroupParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "扩展属性",example = "1")
        private Integer extend;

        @ApiModelProperty(value = "可用区id")
        private String vpcId;

        @ApiModelProperty(value = "是否有效", example = "1")
        private Integer isActive;
    }
}
