package com.baiyi.opscloud.domain.param.datasource;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author 修远
 * @Date 2022/3/24 11:10 AM
 * @Since 1.0
 */
public class DsInstanceScheduleParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AddJob {

        @NotNull(message = "任务类型不能为空")
        private String jobType;

        @NotNull(message = "必须指定数据源实例id")
        private Integer instanceId;

        @NotBlank(message = "资产类型不能为空")
        private String assetType;

        @NotBlank(message = "Cron不能为空")
        private String jobTime;

        private String jobDescription;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UpdateJob {

        @NotNull(message = "任务组不能为空")
        private String group;

        @NotNull(message = "任务名不能为空")
        private String name;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CheckCron {

        @NotNull(message = "Cron不能为空")
        private String jobTime;
    }
}
