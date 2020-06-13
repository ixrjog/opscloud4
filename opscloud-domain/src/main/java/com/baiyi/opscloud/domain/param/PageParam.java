package com.baiyi.opscloud.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class PageParam {

    @ApiModelProperty(value = "分页页码")
    private Integer page;

    @ApiModelProperty(value = "分页页长",example = "10")
    private Integer length;
}
