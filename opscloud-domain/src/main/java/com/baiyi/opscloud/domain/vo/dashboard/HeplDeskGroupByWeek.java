package com.baiyi.opscloud.domain.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/11/23 11:14 上午
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ApiModel
public class HeplDeskGroupByWeek implements Serializable {

    private static final long serialVersionUID = -1127731648922404695L;

    @ApiModelProperty(value = "年周%Y%u")
    private String weeks;

    @ApiModelProperty(value = "桌面维护")
    private int type0;

    @ApiModelProperty(value = "资产管理")
    private int type1;

    @ApiModelProperty(value = "网络管理")
    private int type2;

    @ApiModelProperty(value = "用户管理")
    private int type3;

    @ApiModelProperty(value = "邮箱管理")
    private int type4;

    @ApiModelProperty(value = "IT知识库")
    private int type5;

    @ApiModelProperty(value = "监控管理")
    private int type6;

    @ApiModelProperty(value = "行为管理")
    private int type7;


}

