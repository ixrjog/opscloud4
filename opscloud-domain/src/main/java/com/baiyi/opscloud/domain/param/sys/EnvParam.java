package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:30 下午
 * @Version 1.0
 */
public class EnvParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class EnvPageQuery extends PageParam {

        @ApiModelProperty(value = "环境名称")
        private String envName;

        @ApiModelProperty(value = "环境值")
        private Integer envType;

    }
}
