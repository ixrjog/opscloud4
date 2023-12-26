package com.baiyi.opscloud.domain.param.datasource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2022/3/24 11:10 AM
 * @Since 1.0
 */
public class DsInstanceScheduleParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class AddJob {

        @NotNull(message = "任务类型不能为空")
        private String jobType;

        @NotNull(message = "必须指定数据源实例ID")
        private Integer instanceId;

        @NotBlank(message = "资产类型不能为空")
        private String assetType;

        @NotBlank(message = "Cron不能为空")
        private String jobTime;

        private String jobDescription;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UpdateJob {

        @NotNull(message = "任务组不能为空")
        private String group;

        @NotNull(message = "任务名不能为空")
        private String name;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class DeleteJob {

        @NotNull(message = "任务组不能为空")
        private String group;

        @NotNull(message = "任务名不能为空")
        private String name;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class CheckCron {

        @NotNull(message = "Cron不能为空")
        private String jobTime;
    }

}