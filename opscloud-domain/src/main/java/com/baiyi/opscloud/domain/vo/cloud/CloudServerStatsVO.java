package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/21 2:49 下午
 * @Since 1.0
 */
public class CloudServerStatsVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerMonthStats {
        @ApiModelProperty(value = "日期")
        private String dateCat;

        @ApiModelProperty(value = "vm月度新增数")
        private Integer vm;

        @ApiModelProperty(value = "ecs月度新增数")
        private Integer ecs;

        @ApiModelProperty(value = "ec2月度新增数")
        private Integer ec2;

        @ApiModelProperty(value = "cvm月度新增数")
        private Integer cvm;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerMonthStatsByType {
        @ApiModelProperty(value = "日期")
        private String dateCat;

        @ApiModelProperty(value = "月度新增数")
        private Integer value;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerResStats {

        @ApiModelProperty(value = "cpu总数，单位C")
        private Integer serverTotal;

        @ApiModelProperty(value = "cpu总数，单位C")
        private Integer cpuTotal;

        @ApiModelProperty(value = "内存总数，单位MB")
        private Integer memoryTotal;

        @ApiModelProperty(value = "磁盘总数，单位GB")
        private Integer diskTotal;

        @ApiModelProperty(value = "换算后的内存总数")
        private String memory;

        @ApiModelProperty(value = "换算后的内存总数")
        private String memoryUnit;

        @ApiModelProperty(value = "换算后的磁盘总数")
        private String disk;

        @ApiModelProperty(value = "换算后的磁盘总数")
        private String diskUnit;

    }
}

