package com.baiyi.opscloud.domain.param.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/21 5:30 下午
 * @Since 1.0
 */
public class CloudServerStatsParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class MonthStats{

        @ApiModelProperty(value = "云主机类型")
        private Integer queryYear;

        @ApiModelProperty(value = "服务器状态")
        private Integer cloudServerType;
    }
}
