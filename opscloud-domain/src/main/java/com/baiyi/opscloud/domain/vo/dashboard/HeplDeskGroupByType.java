package com.baiyi.opscloud.domain.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/11/23 2:40 下午
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ApiModel
public class HeplDeskGroupByType implements Serializable {

    private static final long serialVersionUID = 3421581178918076219L;

    @ApiModelProperty(value = "类型")
    private int helpdeskType;

    @ApiModelProperty(value = "总数")
    private int value;

    @ApiModelProperty(value = "名称")
    private String name;
}