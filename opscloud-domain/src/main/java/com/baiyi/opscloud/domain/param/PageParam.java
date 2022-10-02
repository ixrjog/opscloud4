package com.baiyi.opscloud.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;

@Data
@ApiModel
public class PageParam {

    @ApiModelProperty(value = "分页页码")
    private Integer page;

    @Max(value = 50, message = "分页查询最大限制50条记录")
    @ApiModelProperty(value = "分页页长",example = "10")
    private Integer length;

}
