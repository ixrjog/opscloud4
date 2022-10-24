package com.baiyi.opscloud.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;

/**
 * @Author baiyi
 * @Date 2021/12/31 9:36 AM
 * @Version 1.0
 */
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel
public class SuperPageParam {

    @ApiModelProperty(value = "分页页码")
    @Builder.Default
    private Integer page = 1;

    @Max(value = 1024, message = "分页查询最大限制1024条记录")
    @ApiModelProperty(value = "分页页长", example = "10")
    private Integer length;

}
